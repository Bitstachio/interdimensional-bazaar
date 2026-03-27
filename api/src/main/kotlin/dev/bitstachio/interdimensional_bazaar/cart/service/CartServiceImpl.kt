package dev.bitstachio.interdimensional_bazaar.cart.service

import dev.bitstachio.interdimensional_bazaar.cart.domain.Cart
import dev.bitstachio.interdimensional_bazaar.cart.domain.CartItem
import dev.bitstachio.interdimensional_bazaar.cart.dto.AddCartItemRequest
import dev.bitstachio.interdimensional_bazaar.cart.dto.CartLineResponse
import dev.bitstachio.interdimensional_bazaar.cart.dto.CartResponse
import dev.bitstachio.interdimensional_bazaar.cart.dto.UpdateCartItemRequest
import dev.bitstachio.interdimensional_bazaar.cart.exception.CartItemNotFoundException
import dev.bitstachio.interdimensional_bazaar.cart.exception.CartNotFoundException
import dev.bitstachio.interdimensional_bazaar.cart.exception.InsufficientStockException
import dev.bitstachio.interdimensional_bazaar.cart.exception.InvalidQuantityException
import dev.bitstachio.interdimensional_bazaar.cart.exception.ProductNotAvailableException
import dev.bitstachio.interdimensional_bazaar.cart.repository.CartItemRepository
import dev.bitstachio.interdimensional_bazaar.cart.repository.CartRepository
import dev.bitstachio.interdimensional_bazaar.product.domain.Product
import dev.bitstachio.interdimensional_bazaar.product.exception.ProductNotFoundException
import dev.bitstachio.interdimensional_bazaar.product.repository.ProductRepository
import dev.bitstachio.interdimensional_bazaar.user.exception.UserNotFoundException
import dev.bitstachio.interdimensional_bazaar.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Service
@Transactional
class CartServiceImpl(
	private val cartRepository: CartRepository,
	private val cartItemRepository: CartItemRepository,
	private val userRepository: UserRepository,
	private val productRepository: ProductRepository,
) : CartService {

	override fun getCart(userId: UUID): CartResponse {
		val cart = getOrCreateCartForUser(userId)
		return toCartResponse(cart)
	}

	override fun addItem(userId: UUID, request: AddCartItemRequest): CartResponse {
		val cart = getOrCreateCartForUser(userId)
		addItemToCart(cart, request)
		return toCartResponse(reloadCart(cart.id!!))
	}

	override fun updateItem(userId: UUID, itemId: Long, request: UpdateCartItemRequest): CartResponse {
		val cart =
			cartRepository.findByUser_Id(userId).orElseThrow {
				CartNotFoundException("Cart not found for user: $userId")
			}
		updateItemInCart(cart, itemId, request)
		return toCartResponse(reloadCart(cart.id!!))
	}

	override fun removeItem(userId: UUID, itemId: Long): CartResponse {
		val cart =
			cartRepository.findByUser_Id(userId).orElseThrow {
				CartNotFoundException("Cart not found for user: $userId")
			}
		removeItemFromCart(cart, itemId)
		return toCartResponse(reloadCart(cart.id!!))
	}

	override fun createGuestCart(): CartResponse {
		val cart = cartRepository.save(Cart(user = null))
		return toCartResponse(cart)
	}

	override fun getCartById(cartId: UUID): CartResponse {
		val cart =
			cartRepository.findById(cartId).orElseThrow {
				CartNotFoundException("Cart not found: $cartId")
			}
		return toCartResponse(cart)
	}

	override fun addItemByCartId(cartId: UUID, request: AddCartItemRequest): CartResponse {
		val cart =
			cartRepository.findById(cartId).orElseThrow {
				CartNotFoundException("Cart not found: $cartId")
			}
		addItemToCart(cart, request)
		return toCartResponse(reloadCart(cartId))
	}

	override fun updateItemByCartId(cartId: UUID, itemId: Long, request: UpdateCartItemRequest): CartResponse {
		val cart =
			cartRepository.findById(cartId).orElseThrow {
				CartNotFoundException("Cart not found: $cartId")
			}
		updateItemInCart(cart, itemId, request)
		return toCartResponse(reloadCart(cartId))
	}

	override fun removeItemByCartId(cartId: UUID, itemId: Long): CartResponse {
		val cart =
			cartRepository.findById(cartId).orElseThrow {
				CartNotFoundException("Cart not found: $cartId")
			}
		removeItemFromCart(cart, itemId)
		return toCartResponse(reloadCart(cartId))
	}

	private fun getOrCreateCartForUser(userId: UUID): Cart {
		val user = userRepository.findById(userId).orElseThrow { UserNotFoundException(userId) }
		return cartRepository.findByUser_Id(userId).orElseGet {
			cartRepository.save(Cart(user = user))
		}
	}

	private fun reloadCart(cartId: UUID): Cart {
		return cartRepository.findById(cartId).orElseThrow()
	}

	private fun addItemToCart(cart: Cart, request: AddCartItemRequest) {
		requireQuantity(request.quantity)
		val product =
			productRepository.findById(request.productId).orElseThrow {
				ProductNotFoundException(request.productId)
			}
		ensureProductPurchasable(product)
		val cartId = cart.id!!
		val existing = cartItemRepository.findByCart_IdAndProduct_Id(cartId, product.id!!)
		if (existing.isPresent) {
			val line = existing.get()
			val newQuantity = line.quantity + request.quantity
			ensureStock(product, newQuantity)
			line.quantity = newQuantity
		} else {
			ensureStock(product, request.quantity)
			val line = CartItem(cart = cart, product = product, quantity = request.quantity)
			cart.items.add(line)
		}
		cartRepository.save(cart)
	}

	private fun updateItemInCart(cart: Cart, itemId: Long, request: UpdateCartItemRequest) {
		requireQuantity(request.quantity)
		val item =
			cartItemRepository.findByIdAndCart_Id(itemId, cart.id!!).orElseThrow {
				CartItemNotFoundException(itemId)
			}
		val product = item.product
		ensureProductPurchasable(product)
		ensureStock(product, request.quantity)
		item.quantity = request.quantity
		cartRepository.save(cart)
	}

	private fun removeItemFromCart(cart: Cart, itemId: Long) {
		val item =
			cartItemRepository.findByIdAndCart_Id(itemId, cart.id!!).orElseThrow {
				CartItemNotFoundException(itemId)
			}
		cart.items.remove(item)
		cartRepository.save(cart)
	}

	private fun requireQuantity(quantity: Int) {
		if (quantity < 1) {
			throw InvalidQuantityException()
		}
	}

	private fun ensureProductPurchasable(product: Product) {
		if (!product.isActive) {
			throw ProductNotAvailableException(product.id!!)
		}
	}

	private fun ensureStock(product: Product, desiredQuantity: Int) {
		if (desiredQuantity > product.stockQuantity) {
			throw InsufficientStockException(product.id!!, desiredQuantity, product.stockQuantity)
		}
	}

	private fun toCartResponse(cart: Cart): CartResponse {
		val lines =
			cart.items.map { item ->
				val unitPrice = item.product.price
				val lineTotal = unitPrice.multiply(BigDecimal(item.quantity))
				CartLineResponse(
					id = item.id!!,
					productId = item.product.id!!,
					quantity = item.quantity,
					name = item.product.name,
					slug = item.product.slug,
					unitPrice = unitPrice,
					imageUrl = item.product.imageUrl,
					lineTotal = lineTotal,
				)
			}
		val subtotal =
			lines.fold(BigDecimal.ZERO) { acc, line -> acc.add(line.lineTotal) }
		return CartResponse(
			id = cart.id!!,
			userId = cart.user?.id,
			createdAt = cart.createdAt,
			items = lines,
			subtotal = subtotal,
		)
	}
}
