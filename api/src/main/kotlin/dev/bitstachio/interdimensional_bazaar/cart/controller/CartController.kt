package dev.bitstachio.interdimensional_bazaar.cart.controller

import dev.bitstachio.interdimensional_bazaar.cart.dto.AddCartItemRequest
import dev.bitstachio.interdimensional_bazaar.cart.dto.CartResponse
import dev.bitstachio.interdimensional_bazaar.cart.dto.UpdateCartItemRequest
import dev.bitstachio.interdimensional_bazaar.cart.service.CartService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/carts")
class CartController(private val cartService: CartService) {

	@GetMapping("/user/{userId}")
	fun getCart(@PathVariable userId: UUID): CartResponse {
		return cartService.getCart(userId)
	}

	@PostMapping("/user/{userId}/items")
	fun addItem(
		@PathVariable userId: UUID,
		@RequestBody request: AddCartItemRequest,
	): CartResponse {
		return cartService.addItem(userId, request)
	}

	@PutMapping("/user/{userId}/items/{itemId}")
	fun updateItem(
		@PathVariable userId: UUID,
		@PathVariable itemId: Long,
		@RequestBody request: UpdateCartItemRequest,
	): CartResponse {
		return cartService.updateItem(userId, itemId, request)
	}

	@DeleteMapping("/user/{userId}/items/{itemId}")
	fun removeItem(
		@PathVariable userId: UUID,
		@PathVariable itemId: Long,
	): CartResponse {
		return cartService.removeItem(userId, itemId)
	}

	@PostMapping("/guest")
	fun createGuestCart(): CartResponse {
		return cartService.createGuestCart()
	}

	@GetMapping("/{cartId}")
	fun getCartById(@PathVariable cartId: UUID): CartResponse {
		return cartService.getCartById(cartId)
	}

	@PostMapping("/{cartId}/items")
	fun addItemByCartId(
		@PathVariable cartId: UUID,
		@RequestBody request: AddCartItemRequest,
	): CartResponse {
		return cartService.addItemByCartId(cartId, request)
	}

	@PutMapping("/{cartId}/items/{itemId}")
	fun updateItemByCartId(
		@PathVariable cartId: UUID,
		@PathVariable itemId: Long,
		@RequestBody request: UpdateCartItemRequest,
	): CartResponse {
		return cartService.updateItemByCartId(cartId, itemId, request)
	}

	@DeleteMapping("/{cartId}/items/{itemId}")
	fun removeItemByCartId(
		@PathVariable cartId: UUID,
		@PathVariable itemId: Long,
	): CartResponse {
		return cartService.removeItemByCartId(cartId, itemId)
	}
}
