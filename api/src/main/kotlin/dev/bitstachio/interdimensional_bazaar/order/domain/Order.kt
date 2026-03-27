package dev.bitstachio.interdimensional_bazaar.order.domain

import dev.bitstachio.interdimensional_bazaar.user.domain.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
class Order(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	var id: UUID? = null,
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	var user: User? = null,
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	var status: OrderStatus = OrderStatus.PENDING,
	@Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
	var totalAmount: BigDecimal = BigDecimal.ZERO,
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: LocalDateTime = LocalDateTime.now(),
	@Column(name = "updated_at", nullable = false)
	var updatedAt: LocalDateTime = LocalDateTime.now(),
	@OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
	var items: MutableList<OrderItem> = mutableListOf(),
)
