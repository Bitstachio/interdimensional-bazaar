package dev.bitstachio.interdimensional_bazaar.product.dto

import java.math.BigDecimal
import java.util.UUID

data class ProductCreateRequest(
	val categoryId: UUID?,
	val name: String,
	val slug: String,
	val description: String?,
	val price: BigDecimal,
	val stockQuantity: Int,
	val imageUrl: String?,
	val isActive: Boolean = true,
	val sku: String? = null,
	val rating: BigDecimal? = null,
	val dangerLevel: String? = null,
	val sizes: List<String>? = null,
)
