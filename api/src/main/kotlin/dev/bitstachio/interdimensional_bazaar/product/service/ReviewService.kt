package dev.bitstachio.interdimensional_bazaar.product.service

import dev.bitstachio.interdimensional_bazaar.product.dto.ReviewCreateRequest
import dev.bitstachio.interdimensional_bazaar.product.dto.ReviewResponse
import java.util.UUID

interface ReviewService {
    fun getReviewsForProduct(productId: UUID): List<ReviewResponse>
    fun createReview(productId: UUID, request: ReviewCreateRequest): ReviewResponse
}