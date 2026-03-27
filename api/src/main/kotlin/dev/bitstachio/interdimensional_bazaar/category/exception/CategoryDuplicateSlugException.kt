package dev.bitstachio.interdimensional_bazaar.category.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException

class CategoryDuplicateSlugException(slug: String) :
	BadRequestException("Category slug already exists: $slug")
