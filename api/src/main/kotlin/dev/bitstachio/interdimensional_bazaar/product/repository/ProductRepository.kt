package dev.bitstachio.interdimensional_bazaar.product.repository

import dev.bitstachio.interdimensional_bazaar.product.domain.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface ProductRepository : JpaRepository<Product, UUID> {
	fun findBySlug(slug: String): Optional<Product>

	fun existsBySlug(slug: String): Boolean

	fun existsBySlugAndIdNot(slug: String, id: UUID): Boolean

	fun findByIsActive(isActive: Boolean, pageable: Pageable): Page<Product>

	fun findByCategoryIdAndIsActive(
		categoryId: UUID,
		isActive: Boolean,
		pageable: Pageable,
	): Page<Product>

	@Query(
		"""
		SELECT p FROM Product p
		WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
		AND (:activeOnly = false OR p.isActive = true)
		AND (
			:search IS NULL OR :search = ''
			OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(p.slug) LIKE LOWER(CONCAT('%', :search, '%'))
			OR (p.description IS NOT NULL AND LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
		)
		""",
	)
	fun searchProducts(
		@Param("categoryId") categoryId: UUID?,
		@Param("activeOnly") activeOnly: Boolean,
		@Param("search") search: String?,
		pageable: Pageable,
	): Page<Product>
}
