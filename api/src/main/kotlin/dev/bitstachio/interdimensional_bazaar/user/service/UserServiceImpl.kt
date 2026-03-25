package dev.bitstachio.interdimensional_bazaar.user.service

import dev.bitstachio.interdimensional_bazaar.user.domain.User
import dev.bitstachio.interdimensional_bazaar.user.dto.UserCreateRequest
import dev.bitstachio.interdimensional_bazaar.user.dto.UserResponse
import dev.bitstachio.interdimensional_bazaar.user.dto.UserUpdateRequest
import dev.bitstachio.interdimensional_bazaar.user.exception.UserNotFoundException
import dev.bitstachio.interdimensional_bazaar.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

	override fun create(request: UserCreateRequest): UserResponse {
		val user = User(name = request.name, email = request.email)
		return toResponse(userRepository.save(user))
	}

	override fun getById(id: UUID): UserResponse {
		return userRepository.findById(id).map(this::toResponse).orElseThrow { UserNotFoundException(id) }
	}

	override fun getAll(): List<UserResponse> {
		return userRepository.findAll().map(this::toResponse)
	}

	override fun update(id: UUID, request: UserUpdateRequest): UserResponse {
		val user = userRepository.findById(id).orElseThrow { UserNotFoundException(id) }
		user.name = request.name
		user.email = request.email
		return toResponse(userRepository.save(user))
	}

	override fun delete(id: UUID) {
		userRepository.deleteById(id)
	}

	private fun toResponse(user: User): UserResponse {
		return UserResponse(
			id = user.id!!,
			name = user.name,
			email = user.email,
		)
	}
}
