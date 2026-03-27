package dev.bitstachio.interdimensional_bazaar.product.service

import dev.bitstachio.interdimensional_bazaar.product.dto.ProductCreateRequest
import dev.bitstachio.interdimensional_bazaar.product.dto.ProductResponse
import dev.bitstachio.interdimensional_bazaar.product.dto.ProductUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ProductService {
	fun list(categoryId: UUID?, activeOnly: Boolean, pageable: Pageable): Page<ProductResponse>

	fun getById(id: UUID): ProductResponse

	fun getBySlug(slug: String): ProductResponse

	fun create(request: ProductCreateRequest): ProductResponse

	fun update(id: UUID, request: ProductUpdateRequest): ProductResponse

	fun delete(id: UUID)
}
