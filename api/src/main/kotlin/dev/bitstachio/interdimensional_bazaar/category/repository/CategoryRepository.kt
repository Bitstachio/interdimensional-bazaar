package dev.bitstachio.interdimensional_bazaar.category.repository

import dev.bitstachio.interdimensional_bazaar.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface CategoryRepository : JpaRepository<Category, UUID> {
	fun findBySlug(slug: String): Optional<Category>

	fun existsByName(name: String): Boolean

	fun existsByNameAndIdNot(name: String, id: UUID): Boolean

	fun existsBySlug(slug: String): Boolean

	fun existsBySlugAndIdNot(slug: String, id: UUID): Boolean
}
