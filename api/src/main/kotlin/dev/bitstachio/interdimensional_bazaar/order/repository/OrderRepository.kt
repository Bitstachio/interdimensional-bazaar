package dev.bitstachio.interdimensional_bazaar.order.repository

import dev.bitstachio.interdimensional_bazaar.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID
import java.util.Optional

interface OrderRepository : JpaRepository<Order, UUID> {

    /**
     * Find order by ID with items and their products eagerly loaded.
     * Prevents LazyInitializationException when open-in-view is false.
     */
    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.items i
        LEFT JOIN FETCH i.product
        WHERE o.id = :id
    """)
    fun findByIdWithItems(@Param("id") id: UUID): Optional<Order>

    /**
     * Find all orders for a user with items and products eagerly loaded,
     * ordered by creation date descending.
     */
    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.items i
        LEFT JOIN FETCH i.product
        WHERE o.user.id = :userId
        ORDER BY o.createdAt DESC
    """)
    fun findByUser_IdOrderByCreatedAtDesc(@Param("userId") userId: UUID): List<Order>
}
