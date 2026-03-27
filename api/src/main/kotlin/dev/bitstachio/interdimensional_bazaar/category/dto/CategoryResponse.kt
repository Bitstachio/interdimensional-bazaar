package dev.bitstachio.interdimensional_bazaar.category.dto

import java.time.LocalDateTime
import java.util.UUID

data class CategoryResponse(
	val id: UUID,
	val name: String,
	val slug: String,
	val description: String?,
	val createdAt: LocalDateTime,
)
