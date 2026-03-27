package dev.bitstachio.interdimensional_bazaar.category.service

import dev.bitstachio.interdimensional_bazaar.category.domain.Category
import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryCreateRequest
import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryResponse
import dev.bitstachio.interdimensional_bazaar.category.dto.CategoryUpdateRequest
import dev.bitstachio.interdimensional_bazaar.category.exception.CategoryDuplicateNameException
import dev.bitstachio.interdimensional_bazaar.category.exception.CategoryDuplicateSlugException
import dev.bitstachio.interdimensional_bazaar.category.exception.CategoryNotFoundException
import dev.bitstachio.interdimensional_bazaar.category.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {

	override fun create(request: CategoryCreateRequest): CategoryResponse {
		if (categoryRepository.existsByName(request.name)) {
			throw CategoryDuplicateNameException(request.name)
		}
		if (categoryRepository.existsBySlug(request.slug)) {
			throw CategoryDuplicateSlugException(request.slug)
		}
		val category =
			Category(
				name = request.name,
				slug = request.slug,
				description = request.description,
				createdAt = LocalDateTime.now(),
			)
		return toResponse(categoryRepository.save(category))
	}

	@Transactional(readOnly = true)
	override fun getById(id: UUID): CategoryResponse {
		val category = categoryRepository.findById(id).orElseThrow { CategoryNotFoundException(id) }
		return toResponse(category)
	}

	@Transactional(readOnly = true)
	override fun getBySlug(slug: String): CategoryResponse {
		val category =
			categoryRepository.findBySlug(slug).orElseThrow { CategoryNotFoundException(slug) }
		return toResponse(category)
	}

	@Transactional(readOnly = true)
	override fun getAll(): List<CategoryResponse> {
		return categoryRepository.findAll().map(this::toResponse)
	}

	override fun update(id: UUID, request: CategoryUpdateRequest): CategoryResponse {
		val category = categoryRepository.findById(id).orElseThrow { CategoryNotFoundException(id) }
		if (categoryRepository.existsByNameAndIdNot(request.name, id)) {
			throw CategoryDuplicateNameException(request.name)
		}
		if (categoryRepository.existsBySlugAndIdNot(request.slug, id)) {
			throw CategoryDuplicateSlugException(request.slug)
		}
		category.name = request.name
		category.slug = request.slug
		category.description = request.description
		return toResponse(categoryRepository.save(category))
	}

	override fun delete(id: UUID) {
		if (!categoryRepository.existsById(id)) {
			throw CategoryNotFoundException(id)
		}
		categoryRepository.deleteById(id)
	}

	private fun toResponse(category: Category): CategoryResponse {
		return CategoryResponse(
			id = category.id!!,
			name = category.name,
			slug = category.slug,
			description = category.description,
			createdAt = category.createdAt,
		)
	}
}
