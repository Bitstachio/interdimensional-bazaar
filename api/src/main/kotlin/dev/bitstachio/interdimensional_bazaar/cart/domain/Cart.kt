package dev.bitstachio.interdimensional_bazaar.cart.domain

import dev.bitstachio.interdimensional_bazaar.user.domain.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "carts")
class Cart(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	var id: UUID? = null,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	var user: User,
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: LocalDateTime = LocalDateTime.now(),
	@OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], orphanRemoval = true)
	var items: MutableList<CartItem> = mutableListOf(),
)
