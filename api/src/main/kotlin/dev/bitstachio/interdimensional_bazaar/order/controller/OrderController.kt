package dev.bitstachio.interdimensional_bazaar.order.controller

import dev.bitstachio.interdimensional_bazaar.order.dto.OrderResponse
import dev.bitstachio.interdimensional_bazaar.order.dto.PlaceOrderRequest
import dev.bitstachio.interdimensional_bazaar.order.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

	@PostMapping
	fun placeOrder(@RequestBody request: PlaceOrderRequest): OrderResponse {
		return orderService.placeOrder(request)
	}

	@GetMapping("/user/{userId}")
	fun listByUser(@PathVariable userId: UUID): List<OrderResponse> {
		return orderService.listByUser(userId)
	}

	@GetMapping("/{orderId}")
	fun getById(@PathVariable orderId: UUID): OrderResponse {
		return orderService.getById(orderId)
	}
}
