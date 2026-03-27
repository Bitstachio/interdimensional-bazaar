package dev.bitstachio.interdimensional_bazaar.category.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException

class CategoryDuplicateNameException(name: String) :
	BadRequestException("Category name already exists: $name")
