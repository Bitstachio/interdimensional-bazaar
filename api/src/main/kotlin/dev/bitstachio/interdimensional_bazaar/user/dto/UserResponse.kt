package dev.bitstachio.interdimensional_bazaar.user.dto

import java.util.UUID

data class UserResponse(val id: UUID, val name: String, val email: String)
