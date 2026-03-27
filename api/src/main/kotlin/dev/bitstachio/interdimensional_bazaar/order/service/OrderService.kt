package dev.bitstachio.interdimensional_bazaar.order.service

import dev.bitstachio.interdimensional_bazaar.order.dto.OrderResponse
import dev.bitstachio.interdimensional_bazaar.order.dto.PlaceOrderRequest
import java.util.UUID

interface OrderService {
	fun placeOrder(request: PlaceOrderRequest): OrderResponse

	fun getById(orderId: UUID): OrderResponse

	fun listByUser(userId: UUID): List<OrderResponse>
}
