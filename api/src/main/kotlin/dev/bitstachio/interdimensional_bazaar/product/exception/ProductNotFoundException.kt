package dev.bitstachio.interdimensional_bazaar.product.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.ResourceNotFoundException
import java.util.UUID

class ProductNotFoundException : ResourceNotFoundException {
	constructor(id: UUID) : super("Product not found with ID: $id")

	constructor(slug: String) : super("Product not found with slug: $slug")
}
