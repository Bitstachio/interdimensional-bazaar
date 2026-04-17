package dev.bitstachio.interdimensional_bazaar.product.service

import dev.bitstachio.interdimensional_bazaar.product.domain.Review
import dev.bitstachio.interdimensional_bazaar.product.dto.ReviewCreateRequest
import dev.bitstachio.interdimensional_bazaar.product.dto.ReviewResponse
import dev.bitstachio.interdimensional_bazaar.product.exception.DuplicateReviewException
import dev.bitstachio.interdimensional_bazaar.product.exception.ProductNotFoundException
import dev.bitstachio.interdimensional_bazaar.product.repository.ProductRepository
import dev.bitstachio.interdimensional_bazaar.product.repository.ReviewRepository
import dev.bitstachio.interdimensional_bazaar.user.exception.UserNotFoundException
import dev.bitstachio.interdimensional_bazaar.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) : ReviewService {

    @Transactional(readOnly = true)
    override fun getReviewsForProduct(productId: UUID): List<ReviewResponse> =
        reviewRepository.findByProductIdWithUser(productId).map(::toResponse)

    override fun createReview(productId: UUID, request: ReviewCreateRequest): ReviewResponse {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException(productId) }

        val user = userRepository.findById(request.userId)
            .orElseThrow { UserNotFoundException(request.userId) }

        if (reviewRepository.existsByProductIdAndUserId(productId, request.userId)) {
            throw DuplicateReviewException(productId, request.userId)
        }

        val review = Review(
            product = product,
            user = user,
            rating = request.rating,
            title = request.title,
            body = request.body,
            createdAt = LocalDateTime.now(),
        )

        return toResponse(reviewRepository.save(review))
    }

    private fun toResponse(review: Review): ReviewResponse =
        ReviewResponse(
            id = review.id!!,
            userId = review.user.id!!,
            firstName = review.user.firstName,
            lastName = review.user.lastName,
            rating = review.rating,
            title = review.title,
            body = review.body,
            createdAt = review.createdAt,
        )
}
