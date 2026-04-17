package dev.bitstachio.interdimensional_bazaar.user.service

import dev.bitstachio.interdimensional_bazaar.user.domain.User
import dev.bitstachio.interdimensional_bazaar.user.dto.UserCreateRequest
import dev.bitstachio.interdimensional_bazaar.user.dto.UserResponse
import dev.bitstachio.interdimensional_bazaar.user.dto.UserUpdateRequest
import dev.bitstachio.interdimensional_bazaar.user.exception.UserDuplicateEmailException
import dev.bitstachio.interdimensional_bazaar.user.exception.UserNotFoundException
import dev.bitstachio.interdimensional_bazaar.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

	override fun create(request: UserCreateRequest): UserResponse {
		if (userRepository.existsByEmail(request.email)) {
			throw UserDuplicateEmailException(request.email)
		}
		val now = LocalDateTime.now()
		val user =
			User(
				firstName = request.firstName,
				lastName = request.lastName,
				email = request.email,
				passwordHash = request.passwordHash,
				role = request.role,
				phone = request.phone,
				createdAt = now,
			)
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
		if (userRepository.existsByEmailAndIdNot(request.email, id)) {
			throw UserDuplicateEmailException(request.email)
		}
		user.firstName = request.firstName
		user.lastName = request.lastName
		user.email = request.email
		user.passwordHash = request.passwordHash
		user.phone = request.phone
		return toResponse(userRepository.save(user))
	}

	override fun delete(id: UUID) {
		if (!userRepository.existsById(id)) {
			throw UserNotFoundException(id)
		}
		userRepository.deleteById(id)
	}

	private fun toResponse(user: User): UserResponse {
		return UserResponse(
			id = user.id!!,
			firstName = user.firstName,
			lastName = user.lastName,
			email = user.email,
			createdAt = user.createdAt,
			role = user.role,
			phone = user.phone,
		)
	}
}
