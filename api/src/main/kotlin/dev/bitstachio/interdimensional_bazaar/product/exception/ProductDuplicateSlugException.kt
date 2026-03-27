package dev.bitstachio.interdimensional_bazaar.product.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException

class ProductDuplicateSlugException(slug: String) :
	BadRequestException("Product slug already exists: $slug")
