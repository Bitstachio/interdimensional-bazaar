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

    /**
     * Fetch active/inactive products with category eagerly loaded in a single query.
     * JOIN FETCH prevents the N+1 problem where each product fires a separate
     * category SELECT.
     */
    @Query(
        value = """
            SELECT p FROM Product p
            LEFT JOIN FETCH p.category
            WHERE p.isActive = :isActive
        """,
        countQuery = """
            SELECT COUNT(p) FROM Product p
            WHERE p.isActive = :isActive
        """,
    )
    fun findByIsActive(
        @Param("isActive") isActive: Boolean,
        pageable: Pageable,
    ): Page<Product>

    /**
     * Fetch products by category with category eagerly loaded in a single query.
     */
    @Query(
        value = """
            SELECT p FROM Product p
            LEFT JOIN FETCH p.category
            WHERE p.category.id = :categoryId
            AND p.isActive = :isActive
        """,
        countQuery = """
            SELECT COUNT(p) FROM Product p
            WHERE p.category.id = :categoryId
            AND p.isActive = :isActive
        """,
    )
    fun findByCategoryIdAndIsActive(
        @Param("categoryId") categoryId: UUID,
        @Param("isActive") isActive: Boolean,
        pageable: Pageable,
    ): Page<Product>

    /**
     * Full-text search across name, slug, and description with category
     * eagerly loaded in a single query.
     */
    @Query(
        value = """
            SELECT p FROM Product p
            LEFT JOIN FETCH p.category
            WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
            AND (:activeOnly = false OR p.isActive = true)
            AND (
                :search IS NULL OR :search = ''
                OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(p.slug) LIKE LOWER(CONCAT('%', :search, '%'))
                OR (p.description IS NOT NULL AND LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
            )
        """,
        countQuery = """
            SELECT COUNT(p) FROM Product p
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
