package dev.bitstachio.interdimensional_bazaar.order.service

import dev.bitstachio.interdimensional_bazaar.cart.exception.CartNotFoundException
import dev.bitstachio.interdimensional_bazaar.cart.exception.ProductNotAvailableException
import dev.bitstachio.interdimensional_bazaar.cart.exception.InsufficientStockException
import dev.bitstachio.interdimensional_bazaar.cart.repository.CartRepository
import dev.bitstachio.interdimensional_bazaar.order.domain.Order
import dev.bitstachio.interdimensional_bazaar.order.domain.OrderItem
import dev.bitstachio.interdimensional_bazaar.order.domain.OrderStatus
import dev.bitstachio.interdimensional_bazaar.order.dto.OrderLineResponse
import dev.bitstachio.interdimensional_bazaar.order.dto.OrderResponse
import dev.bitstachio.interdimensional_bazaar.order.dto.PlaceOrderRequest
import dev.bitstachio.interdimensional_bazaar.order.exception.EmptyCartException
import dev.bitstachio.interdimensional_bazaar.order.exception.OrderNotFoundException
import dev.bitstachio.interdimensional_bazaar.order.repository.OrderRepository
import dev.bitstachio.interdimensional_bazaar.product.domain.Product
import dev.bitstachio.interdimensional_bazaar.product.exception.ProductNotFoundException
import dev.bitstachio.interdimensional_bazaar.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class OrderServiceImpl(
	private val orderRepository: OrderRepository,
	private val cartRepository: CartRepository,
	private val productRepository: ProductRepository,
) : OrderService {

	override fun placeOrder(request: PlaceOrderRequest): OrderResponse {
		val cart =
			cartRepository.findById(request.cartId).orElseThrow {
				CartNotFoundException("Cart not found: ${request.cartId}")
			}
		if (cart.items.isEmpty()) {
			throw EmptyCartException()
		}
		val now = LocalDateTime.now()
		val order = Order(user = cart.user, status = OrderStatus.PENDING, createdAt = now, updatedAt = now)
		var total = BigDecimal.ZERO
		for (cartItem in cart.items) {
			val product =
				productRepository.findById(cartItem.product.id!!).orElseThrow {
					ProductNotFoundException(cartItem.product.id!!)
				}
			ensureProductPurchasable(product)
			ensureStock(product, cartItem.quantity)
			val lineTotal = product.price.multiply(BigDecimal(cartItem.quantity))
			total = total.add(lineTotal)
			val orderItem =
				OrderItem(
					order = order,
					product = product,
					quantity = cartItem.quantity,
					unitPrice = product.price,
					productName = product.name,
					productSlug = product.slug,
				)
			order.items.add(orderItem)
		}
		order.totalAmount = total
		orderRepository.save(order)
		for (cartItem in cart.items.toList()) {
			val product =
				productRepository.findById(cartItem.product.id!!).orElseThrow {
					ProductNotFoundException(cartItem.product.id!!)
				}
			product.stockQuantity -= cartItem.quantity
			product.updatedAt = now
			productRepository.save(product)
		}
		cart.items.clear()
		cartRepository.save(cart)
		return toResponse(orderRepository.findByIdWithItems(order.id!!).orElseThrow {
		OrderNotFoundException(order.id!!)
		})
	}

	@Transactional(readOnly = true)
	override fun getById(orderId: UUID): OrderResponse {
		val order = orderRepository.findByIdWithItems(orderId).orElseThrow { OrderNotFoundException(orderId) }
		return toResponse(order)
	}

	@Transactional(readOnly = true)
	override fun listByUser(userId: UUID): List<OrderResponse> {
		return orderRepository.findByUser_IdOrderByCreatedAtDesc(userId).map(this::toResponse)
	}

	private fun ensureProductPurchasable(product: Product) {
		if (!product.isActive) {
			throw ProductNotAvailableException(product.id!!)
		}
	}

	private fun ensureStock(product: Product, quantity: Int) {
		if (quantity > product.stockQuantity) {
			throw InsufficientStockException(product.id!!, quantity, product.stockQuantity)
		}
	}

	private fun toResponse(order: Order): OrderResponse {
		val lines =
			order.items.map { item ->
				val lineTotal = item.unitPrice.multiply(BigDecimal(item.quantity))
				OrderLineResponse(
					id = item.id!!,
					productId = item.product.id!!,
					quantity = item.quantity,
					unitPrice = item.unitPrice,
					productName = item.productName,
					productSlug = item.productSlug,
					lineTotal = lineTotal,
				)
			}
		return OrderResponse(
			id = order.id!!,
			userId = order.user?.id,
			status = order.status,
			totalAmount = order.totalAmount,
			createdAt = order.createdAt,
			updatedAt = order.updatedAt,
			items = lines,
		)
	}
}
