package dev.bitstachio.interdimensional_bazaar.cart.repository

import dev.bitstachio.interdimensional_bazaar.cart.domain.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface CartItemRepository : JpaRepository<CartItem, Long> {

    fun findByCart_IdAndProduct_Id(
        cartId: UUID,
        productId: UUID,
    ): Optional<CartItem>

    /**
     * Find cart item by ID and cart ID with product eagerly loaded.
     * JOIN FETCH prevents LazyInitializationException when open-in-view
     * is false and item.product is accessed in updateItemInCart().
     */
    @Query("""
        SELECT i FROM CartItem i
        LEFT JOIN FETCH i.product
        WHERE i.id = :itemId
        AND i.cart.id = :cartId
    """)
    fun findByIdAndCart_Id(
        @Param("itemId") itemId: Long,
        @Param("cartId") cartId: UUID,
    ): Optional<CartItem>
}