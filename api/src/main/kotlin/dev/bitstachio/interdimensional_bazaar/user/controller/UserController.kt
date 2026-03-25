package dev.bitstachio.interdimensional_bazaar.user.controller

import dev.bitstachio.interdimensional_bazaar.user.dto.UserCreateRequest
import dev.bitstachio.interdimensional_bazaar.user.dto.UserResponse
import dev.bitstachio.interdimensional_bazaar.user.dto.UserUpdateRequest
import dev.bitstachio.interdimensional_bazaar.user.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

	@PostMapping
	fun create(@RequestBody request: UserCreateRequest): UserResponse {
		return userService.create(request)
	}

	@GetMapping("/{id}")
	fun getById(@PathVariable id: UUID): UserResponse {
		return userService.getById(id)
	}

	@GetMapping
	fun getAll(): List<UserResponse> {
		return userService.getAll()
	}

	@PutMapping("/{id}")
	fun update(@PathVariable id: UUID, @RequestBody request: UserUpdateRequest): UserResponse {
		return userService.update(id, request)
	}

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id: UUID) {
		userService.delete(id)
	}
}
