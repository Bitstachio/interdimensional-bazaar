package dev.bitstachio.interdimensional_bazaar.product.exception

import java.util.UUID

/**
 * DuplicateReviewException — mapped to 409 Conflict in GlobalExceptionHandler.
 */
class DuplicateReviewException(productId: UUID, userId: UUID) :
    RuntimeException("User $userId has already reviewed product $productId")
    