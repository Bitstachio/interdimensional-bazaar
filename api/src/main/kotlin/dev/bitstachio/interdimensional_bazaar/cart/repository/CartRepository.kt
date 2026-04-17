package dev.bitstachio.interdimensional_bazaar.cart.repository

import dev.bitstachio.interdimensional_bazaar.cart.domain.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface CartRepository : JpaRepository<Cart, UUID> {

    /**
     * Find cart by user ID with items and their products eagerly loaded.
     * JOIN FETCH prevents LazyInitializationException when open-in-view is false.
     */
    @Query("""
        SELECT c FROM Cart c
        LEFT JOIN FETCH c.items i
        LEFT JOIN FETCH i.product
        WHERE c.user.id = :userId
    """)
    fun findByUser_Id(@Param("userId") userId: UUID): Optional<Cart>

    /**
     * Find cart by ID with items and their products eagerly loaded.
     */
    @Query("""
        SELECT c FROM Cart c
        LEFT JOIN FETCH c.items i
        LEFT JOIN FETCH i.product
        WHERE c.id = :id
    """)
    fun findByIdWithItems(@Param("id") id: UUID): Optional<Cart>
}
