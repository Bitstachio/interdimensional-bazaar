package dev.bitstachio.interdimensional_bazaar.cart.repository

import dev.bitstachio.interdimensional_bazaar.cart.domain.Cart
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface CartRepository : JpaRepository<Cart, UUID> {
	fun findByUser_Id(userId: UUID): Optional<Cart>
}
