package dev.bitstachio.interdimensional_bazaar.category.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.ResourceNotFoundException
import java.util.UUID

class CategoryNotFoundException : ResourceNotFoundException {
	constructor(id: UUID) : super("Category not found with ID: $id")

	constructor(slug: String) : super("Category not found with slug: $slug")
}
