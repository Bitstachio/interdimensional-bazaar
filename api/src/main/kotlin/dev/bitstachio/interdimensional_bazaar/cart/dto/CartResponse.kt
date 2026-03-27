package dev.bitstachio.interdimensional_bazaar.cart.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class CartResponse(
	val id: UUID,
	val userId: UUID?,
	val createdAt: LocalDateTime,
	val items: List<CartLineResponse>,
	val subtotal: BigDecimal,
)
