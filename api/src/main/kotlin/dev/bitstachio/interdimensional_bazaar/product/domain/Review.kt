package dev.bitstachio.interdimensional_bazaar.product.domain

import dev.bitstachio.interdimensional_bazaar.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.util.UUID

/**
 * Review — Represents a product review written by a logged-in user.
 *
 * Products can have multiple reviews, each written by a different user.
 * The unique constraint ensures one review per user per product.
 * Unidirectional relationships — neither Product nor User has a
 * back-reference collection to reviews.
 *
 * TODO: BACKEND — When real API is ready, add a service method that
 * recalculates products.rating from the average of all reviews for
 * that product whenever a review is inserted, updated, or deleted.
 */
@Entity
@Table(
    name = "reviews",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_review_user_product", columnNames = ["product_id", "user_id"]),
    ],
)
class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    @Column(nullable = false)
    var rating: Int = 0,
    @Column
    var title: String? = null,
    @Column(columnDefinition = "TEXT")
    var body: String? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
)
