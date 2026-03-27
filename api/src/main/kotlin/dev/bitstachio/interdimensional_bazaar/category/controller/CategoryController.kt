package dev.bitstachio.interdimensional_bazaar.category.controller

import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryCreateRequest
import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryResponse
import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryUpdateRequest
import dev.bitstachio.interdimensional_bazaar.category.service.CategoryService
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
@RequestMapping("/api/categories")
class CategoryController(private val categoryService: CategoryService) {

	@PostMapping
	fun create(@RequestBody request: CategoryCreateRequest): CategoryResponse {
		return categoryService.create(request)
	}

	@GetMapping("/{id}")
	fun getById(@PathVariable id: UUID): CategoryResponse {
		return categoryService.getById(id)
	}

	@GetMapping("/slug/{slug}")
	fun getBySlug(@PathVariable slug: String): CategoryResponse {
		return categoryService.getBySlug(slug)
	}

	@GetMapping
	fun getAll(): List<CategoryResponse> {
		return categoryService.getAll()
	}

	@PutMapping("/{id}")
	fun update(
		@PathVariable id: UUID,
		@RequestBody request: CategoryUpdateRequest,
	): CategoryResponse {
		return categoryService.update(id, request)
	}

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id: UUID) {
		categoryService.delete(id)
	}
}
