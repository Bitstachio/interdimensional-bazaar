package dev.bitstachio.interdimensional_bazaar.cart.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.ResourceNotFoundException
import java.util.UUID

class CartNotFoundException(userId: UUID) :
	ResourceNotFoundException("Cart not found for user: $userId")
