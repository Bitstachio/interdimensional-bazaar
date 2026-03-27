package dev.bitstachio.interdimensional_bazaar.cart.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException
import java.util.UUID

class InsufficientStockException(productId: UUID, requested: Int, available: Int) :
	BadRequestException(
		"Insufficient stock for product $productId: requested $requested, available $available",
	)
