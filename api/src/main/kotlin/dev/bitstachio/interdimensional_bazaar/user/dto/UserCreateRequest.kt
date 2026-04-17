package dev.bitstachio.interdimensional_bazaar.user.dto

import dev.bitstachio.interdimensional_bazaar.user.domain.UserRole

data class UserCreateRequest(
	val firstName: String?,
	val lastName: String?,
	val email: String,
	val passwordHash: String,
	val role: UserRole = UserRole.CUSTOMER,
	val phone: String? = null,
)
