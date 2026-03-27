package dev.bitstachio.interdimensional_bazaar.cart.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException

class InvalidQuantityException : BadRequestException("Quantity must be at least 1")
