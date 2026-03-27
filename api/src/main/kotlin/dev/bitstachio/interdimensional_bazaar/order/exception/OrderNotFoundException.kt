package dev.bitstachio.interdimensional_bazaar.order.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.ResourceNotFoundException
import java.util.UUID

class OrderNotFoundException(orderId: UUID) :
	ResourceNotFoundException("Order not found with ID: $orderId")
