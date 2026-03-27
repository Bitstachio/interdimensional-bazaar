package dev.bitstachio.interdimensional_bazaar.cart.service

import dev.bitstachio.interdimensional_bazaar.cart.dto.AddCartItemRequest
import dev.bitstachio.interdimensional_bazaar.cart.dto.CartResponse
import dev.bitstachio.interdimensional_bazaar.cart.dto.UpdateCartItemRequest
import java.util.UUID

interface CartService {
	fun getCart(userId: UUID): CartResponse

	fun addItem(userId: UUID, request: AddCartItemRequest): CartResponse

	fun updateItem(userId: UUID, itemId: Long, request: UpdateCartItemRequest): CartResponse

	fun removeItem(userId: UUID, itemId: Long): CartResponse
}
