package dev.bitstachio.interdimensional_bazaar.category.service

import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryCreateRequest
import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryResponse
import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryUpdateRequest
import java.util.UUID

interface CategoryService {
	fun create(request: CategoryCreateRequest): CategoryResponse

	fun getById(id: UUID): CategoryResponse

	fun getBySlug(slug: String): CategoryResponse

	fun getAll(): List<CategoryResponse>

	fun update(id: UUID, request: CategoryUpdateRequest): CategoryResponse

	fun delete(id: UUID)
}
