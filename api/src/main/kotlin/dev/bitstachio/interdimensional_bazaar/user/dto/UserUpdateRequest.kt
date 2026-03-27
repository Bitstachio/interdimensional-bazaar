package dev.bitstachio.interdimensional_bazaar.user.dto

data class UserUpdateRequest(
	val firstName: String?,
	val lastName: String?,
	val email: String,
	val passwordHash: String,
)
