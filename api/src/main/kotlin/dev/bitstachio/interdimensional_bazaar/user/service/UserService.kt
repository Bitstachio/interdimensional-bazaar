package dev.bitstachio.interdimensional_bazaar.user.service

import dev.bitstachio.interdimensional_bazaar.user.dto.UserCreateRequest
import dev.bitstachio.interdimensional_bazaar.user.dto.UserResponse
import dev.bitstachio.interdimensional_bazaar.user.dto.UserUpdateRequest
import java.util.UUID

interface UserService {
	fun create(request: UserCreateRequest): UserResponse

	fun getById(id: UUID): UserResponse

	fun getAll(): List<UserResponse>

	fun update(id: UUID, request: UserUpdateRequest): UserResponse

	fun delete(id: UUID)
}
