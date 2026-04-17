package dev.bitstachio.interdimensional_bazaar.user.domain

/**
 * UserRole — Enum representing the role of a user in the system.
 *
 * Roles:
 *   - CUSTOMER: standard user, can browse, add to cart, checkout, write reviews
 *   - ADMIN: elevated access to the admin dashboard and product management
 */
enum class UserRole(val dbValue: String) {
    CUSTOMER("customer"),
    ADMIN("admin");

    companion object {
        fun fromDbValue(value: String): UserRole =
            entries.find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown user role: $value")
    }
}
