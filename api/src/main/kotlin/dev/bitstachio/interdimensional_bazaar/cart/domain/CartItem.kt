package dev.bitstachio.interdimensional_bazaar.cart.domain

import dev.bitstachio.interdimensional_bazaar.product.domain.Product
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
	name = "cart_items",
	uniqueConstraints = [
		UniqueConstraint(name = "uk_cart_product", columnNames = ["cart_id", "product_id"]),
	],
)
class CartItem(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	var cart: Cart,
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	var product: Product,
	@Column(nullable = false)
	var quantity: Int = 1,
)
