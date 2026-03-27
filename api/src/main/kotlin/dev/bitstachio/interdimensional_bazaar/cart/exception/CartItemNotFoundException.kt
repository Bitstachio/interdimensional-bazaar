package dev.bitstachio.interdimensional_bazaar.cart.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.ResourceNotFoundException

class CartItemNotFoundException(itemId: Long) :
	ResourceNotFoundException("Cart line item not found with ID: $itemId")
