package dev.bitstachio.interdimensional_bazaar.order.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException

class EmptyCartException :
	BadRequestException("Cannot place an order from an empty cart")
