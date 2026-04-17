package dev.bitstachio.interdimensional_bazaar.product.controller

import dev.bitstachio.interdimensional_bazaar.product.dto.ProductCreateRequest
import dev.bitstachio.interdimensional_bazaar.product.dto.ProductResponse
import dev.bitstachio.interdimensional_bazaar.product.dto.ProductUpdateRequest
import dev.bitstachio.interdimensional_bazaar.product.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import dev.bitstachio.interdimensional_bazaar.product.dto.ReviewCreateRequest
import dev.bitstachio.interdimensional_bazaar.product.dto.ReviewResponse
import dev.bitstachio.interdimensional_bazaar.product.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/products")
class ProductController(
	private val productService: ProductService,
	private val reviewService: ReviewService,
) {

	@GetMapping
	fun list(
		@RequestParam(required = false) categoryId: UUID?,
		@RequestParam(defaultValue = "true") activeOnly: Boolean,
		@RequestParam(required = false) search: String?,
		@PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
		pageable: Pageable,
	): Page<ProductResponse> {
		return productService.list(categoryId, activeOnly, search, pageable)
	}

	@GetMapping("/{id}")
	fun getById(@PathVariable id: UUID): ProductResponse {
		return productService.getById(id)
	}

	@GetMapping("/slug/{slug}")
	fun getBySlug(@PathVariable slug: String): ProductResponse {
		return productService.getBySlug(slug)
	}

	@PostMapping
	fun create(@RequestBody request: ProductCreateRequest): ProductResponse {
		return productService.create(request)
	}

	@PutMapping("/{id}")
	fun update(
		@PathVariable id: UUID,
		@RequestBody request: ProductUpdateRequest,
	): ProductResponse {
		return productService.update(id, request)
	}

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id: UUID) {
		productService.delete(id)
	}

	@PostMapping("/{id}/reviews")
	@ResponseStatus(HttpStatus.CREATED)
	fun createReview(
		@PathVariable id: UUID,
		@RequestBody @Valid request: ReviewCreateRequest,
	): ReviewResponse = reviewService.createReview(id, request)
}
