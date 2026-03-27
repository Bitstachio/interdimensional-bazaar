package dev.bitstachio.interdimensional_bazaar.product.domain

import dev.bitstachio.interdimensional_bazaar.category.domain.Category
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "products")
class Product(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	var id: UUID? = null,
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	var category: Category? = null,
	@Column(nullable = false)
	var name: String = "",
	@Column(nullable = false, unique = true)
	var slug: String = "",
	@Column(columnDefinition = "TEXT")
	var description: String? = null,
	@Column(nullable = false, precision = 10, scale = 2)
	var price: BigDecimal = BigDecimal.ZERO,
	@Column(name = "stock_quantity", nullable = false)
	var stockQuantity: Int = 0,
	@Column(name = "image_url")
	var imageUrl: String? = null,
	@Column(name = "is_active", nullable = false)
	var isActive: Boolean = true,
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: LocalDateTime = LocalDateTime.now(),
	@Column(name = "updated_at", nullable = false)
	var updatedAt: LocalDateTime = LocalDateTime.now(),
)
