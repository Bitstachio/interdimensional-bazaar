package dev.bitstachio.interdimensional_bazaar.order.dto

import java.math.BigDecimal
import java.util.UUID

data class OrderLineResponse(
	val id: Long,
	val productId: UUID,
	val quantity: Int,
	val unitPrice: BigDecimal,
	val productName: String,
	val productSlug: String,
	val lineTotal: BigDecimal,
)
