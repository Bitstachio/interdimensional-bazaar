package dev.bitstachio.interdimensional_bazaar.product.dto

import java.time.LocalDateTime
import java.util.UUID

/**
 * ReviewResponse — DTO for a single product review.
 * Included as a list inside ProductResponse so reviews are
 * returned alongside product details in a single API call.
 */
data class ReviewResponse(
    val id: UUID,
    val userId: UUID,
    val firstName: String?,
    val lastName: String?,
    val rating: Int,
    val title: String?,
    val body: String?,
    val createdAt: LocalDateTime,
)