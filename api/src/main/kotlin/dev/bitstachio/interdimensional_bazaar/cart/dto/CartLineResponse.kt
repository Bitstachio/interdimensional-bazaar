package dev.bitstachio.interdimensional_bazaar.cart.dto

import java.math.BigDecimal
import java.util.UUID

data class CartLineResponse(
	val id: Long,
	val productId: UUID,
	val quantity: Int,
	val name: String,
	val slug: String,
	val unitPrice: BigDecimal,
	val imageUrl: String?,
	val lineTotal: BigDecimal,
)
