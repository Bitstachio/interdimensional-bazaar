package dev.bitstachio.interdimensional_bazaar.order.repository

import dev.bitstachio.interdimensional_bazaar.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderRepository : JpaRepository<Order, UUID> {
	fun findByUser_IdOrderByCreatedAtDesc(userId: UUID): List<Order>
}
