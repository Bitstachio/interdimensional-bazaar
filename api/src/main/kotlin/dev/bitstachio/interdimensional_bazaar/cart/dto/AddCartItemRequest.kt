package dev.bitstachio.interdimensional_bazaar.cart.dto

import java.util.UUID

data class AddCartItemRequest(val productId: UUID, val quantity: Int)
