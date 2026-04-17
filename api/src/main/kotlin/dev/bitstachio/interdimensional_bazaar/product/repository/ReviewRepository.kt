package dev.bitstachio.interdimensional_bazaar.product.repository

import dev.bitstachio.interdimensional_bazaar.product.domain.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ReviewRepository : JpaRepository<Review, UUID> {

    /**
     * Find all reviews for a product with user eagerly loaded.
     * JOIN FETCH prevents LazyInitializationException when
     * open-in-view is false and reviewer name is accessed.
     */
    @Query("""
        SELECT r FROM Review r
        LEFT JOIN FETCH r.user
        WHERE r.product.id = :productId
        ORDER BY r.createdAt DESC
    """)
    fun findByProductIdWithUser(
        @Param("productId") productId: UUID,
    ): List<Review>

    /**
     * Check if a user has already reviewed a specific product.
     * Used to enforce the one-review-per-user-per-product rule.
     */
    fun existsByProductIdAndUserId(productId: UUID, userId: UUID): Boolean
}
