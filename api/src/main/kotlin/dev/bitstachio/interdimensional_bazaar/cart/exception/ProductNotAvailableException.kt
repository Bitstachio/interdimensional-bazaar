package dev.bitstachio.interdimensional_bazaar.cart.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException
import java.util.UUID

class ProductNotAvailableException(productId: UUID) :
	BadRequestException("Product is not available for purchase: $productId")
