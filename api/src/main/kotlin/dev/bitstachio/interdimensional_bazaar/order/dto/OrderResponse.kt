package dev.bitstachio.interdimensional_bazaar.order.dto

import dev.bitstachio.interdimensional_bazaar.order.domain.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class OrderResponse(
	val id: UUID,
	val userId: UUID?,
	val status: OrderStatus,
	val totalAmount: BigDecimal,
	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime,
	val items: List<OrderLineResponse>,
)
