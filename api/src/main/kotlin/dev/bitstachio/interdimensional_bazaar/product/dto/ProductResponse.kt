package dev.bitstachio.interdimensional_bazaar.product.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class ProductResponse(
	val id: UUID,
	val categoryId: UUID?,
	val categoryName: String?,
	val name: String,
	val slug: String,
	val description: String?,
	val price: BigDecimal,
	val stockQuantity: Int,
	val imageUrl: String?,
	val isActive: Boolean,
	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime,
	val sku: String?,
	val rating: java.math.BigDecimal?,
	val dangerLevel: String?,
	val sizes: List<String>?,
)
