package dev.bitstachio.interdimensional_bazaar.cart.repository

import dev.bitstachio.interdimensional_bazaar.cart.domain.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface CartItemRepository : JpaRepository<CartItem, Long> {
	fun findByCart_IdAndProduct_Id(cartId: UUID, productId: UUID): Optional<CartItem>

	fun findByIdAndCart_Id(itemId: Long, cartId: UUID): Optional<CartItem>
}
