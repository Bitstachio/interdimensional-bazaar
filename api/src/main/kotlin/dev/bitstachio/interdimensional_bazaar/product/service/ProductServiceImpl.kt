package dev.bitstachio.interdimensional_bazaar.product.service

import com.fasterxml.jackson.databind.ObjectMapper
import dev.bitstachio.interdimensional_bazaar.category.domain.Category
import dev.bitstachio.interdimensional_bazaar.category.exception.CategoryNotFoundException
import dev.bitstachio.interdimensional_bazaar.category.repository.CategoryRepository
import dev.bitstachio.interdimensional_bazaar.product.domain.DangerLevel
import dev.bitstachio.interdimensional_bazaar.product.domain.Product
import dev.bitstachio.interdimensional_bazaar.product.dto.ProductCreateRequest
import dev.bitstachio.interdimensional_bazaar.product.dto.ProductResponse
import dev.bitstachio.interdimensional_bazaar.product.dto.ProductUpdateRequest
import dev.bitstachio.interdimensional_bazaar.product.exception.ProductDuplicateSlugException
import dev.bitstachio.interdimensional_bazaar.product.exception.ProductNotFoundException
import dev.bitstachio.interdimensional_bazaar.product.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val reviewService: ReviewService,
) : ProductService {

    private val objectMapper = ObjectMapper()

    @Transactional(readOnly = true)
    override fun list(
        categoryId: UUID?,
        activeOnly: Boolean,
        search: String?,
        pageable: Pageable,
    ): Page<ProductResponse> {
        val normalizedSearch = search?.trim()?.takeIf { it.isNotEmpty() }
        val page =
            if (normalizedSearch != null) {
                productRepository.searchProducts(categoryId, activeOnly, normalizedSearch, pageable)
            } else if (categoryId != null) {
                productRepository.findByCategoryIdAndIsActive(categoryId, activeOnly, pageable)
            } else {
                productRepository.findByIsActive(activeOnly, pageable)
            }
        return page.map(this::toResponse)
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): ProductResponse {
        val product = productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
        return toResponse(product)
    }

    @Transactional(readOnly = true)
    override fun getBySlug(slug: String): ProductResponse {
        val product =
            productRepository.findBySlug(slug).orElseThrow { ProductNotFoundException(slug) }
        if (!product.isActive) {
            throw ProductNotFoundException(slug)
        }
        return toResponse(product)
    }

    override fun create(request: ProductCreateRequest): ProductResponse {
        if (productRepository.existsBySlug(request.slug)) {
            throw ProductDuplicateSlugException(request.slug)
        }
        val now = LocalDateTime.now()
        val product = Product(
            category = resolveCategory(request.categoryId),
            name = request.name,
            slug = request.slug,
            description = request.description,
            price = request.price,
            stockQuantity = request.stockQuantity,
            imageUrl = request.imageUrl,
            isActive = request.isActive,
            createdAt = now,
            updatedAt = now,
            sku = request.sku,
            rating = request.rating,
            dangerLevel = request.dangerLevel?.let { DangerLevel.fromDbValue(it) },
            sizesJson = request.sizes?.let { objectMapper.writeValueAsString(it) },
        )
        return toResponse(productRepository.save(product))
    }

    override fun update(id: UUID, request: ProductUpdateRequest): ProductResponse {
        val product = productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
        if (productRepository.existsBySlugAndIdNot(request.slug, id)) {
            throw ProductDuplicateSlugException(request.slug)
        }
        product.category = resolveCategory(request.categoryId)
        product.name = request.name
        product.slug = request.slug
        product.description = request.description
        product.price = request.price
        product.stockQuantity = request.stockQuantity
        product.imageUrl = request.imageUrl
        product.isActive = request.isActive
        product.sku = request.sku
        product.rating = request.rating
        product.dangerLevel = request.dangerLevel?.let { DangerLevel.fromDbValue(it) }
        product.sizesJson = request.sizes?.let { objectMapper.writeValueAsString(it) }
        product.updatedAt = LocalDateTime.now()
        return toResponse(productRepository.save(product))
    }

    override fun delete(id: UUID) {
        if (!productRepository.existsById(id)) {
            throw ProductNotFoundException(id)
        }
        productRepository.deleteById(id)
    }

    private fun resolveCategory(categoryId: UUID?): Category? {
        if (categoryId == null) return null
        return categoryRepository.findById(categoryId).orElseThrow {
            CategoryNotFoundException(categoryId)
        }
    }

    private fun toResponse(product: Product): ProductResponse =
        ProductResponse(
            id = product.id!!,
            categoryId = product.category?.id,
            categoryName = product.category?.name,
            name = product.name,
            slug = product.slug,
            description = product.description,
            price = product.price,
            stockQuantity = product.stockQuantity,
            imageUrl = product.imageUrl,
            isActive = product.isActive,
            sku = product.sku,
            rating = product.rating,
            dangerLevel = product.dangerLevel?.dbValue,
            sizes = product.sizes,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt,
            reviews = reviewService.getReviewsForProduct(product.id!!),
        )
}
