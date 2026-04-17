package dev.bitstachio.interdimensional_bazaar.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * Address — Represents a saved address for a user.
 *
 * Users can save multiple addresses for delivery and billing purposes.
 * Each address can be flagged as the default delivery address, default
 * billing address, or both. Unidirectional relationship to User —
 * User entity has no back-reference to addresses.
 *
 * TODO: APPLICATION LEVEL — enforce single default per type in
 * useCheckout.ts and account address management hooks.
 */
@Entity
@Table(name = "addresses")
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    @Column(name = "full_name", nullable = false)
    var fullName: String = "",
    @Column(nullable = false)
    var street: String = "",
    @Column(nullable = false)
    var city: String = "",
    @Column(name = "province_state", nullable = false)
    var provinceState: String = "",
    @Column(nullable = false, length = 2)
    var country: String = "",
    @Column(name = "postal_code", nullable = false)
    var postalCode: String = "",
    @Column(name = "is_default_delivery", nullable = false)
    var isDefaultDelivery: Boolean = false,
    @Column(name = "is_default_billing", nullable = false)
    var isDefaultBilling: Boolean = false,
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
)