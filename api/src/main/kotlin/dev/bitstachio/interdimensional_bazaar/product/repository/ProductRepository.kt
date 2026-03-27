package dev.bitstachio.interdimensional_bazaar.product.repository

import dev.bitstachio.interdimensional_bazaar.product.domain.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
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
}
