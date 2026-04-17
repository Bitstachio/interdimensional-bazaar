package dev.bitstachio.interdimensional_bazaar.product.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.UUID

/**
 * ReviewCreateRequest — DTO for submitting a new product review.
 *
 * TODO: BACKEND — userId is currently passed explicitly in the request
 * body as a placeholder. Once JWT authentication is implemented, remove
 * userId from this DTO and derive it from the authenticated principal
 * in ReviewController instead.
 */
data class ReviewCreateRequest(
    @field:NotNull(message = "User ID is required")
    val userId: UUID,

    @field:NotNull(message = "Rating is required")
    @field:Min(value = 1, message = "Rating must be at least 1")
    @field:Max(value = 5, message = "Rating must be at most 5")
    val rating: Int,

    val title: String?,
    val body: String?,
)
