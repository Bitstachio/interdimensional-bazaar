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
import jakarta.persistence.Convert
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import com.fasterxml.jackson.databind.ObjectMapper

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
	@Column(name = "sku", unique = true)
	var sku: String? = null,
	@Column(name = "rating", precision = 2, scale = 1)
	var rating: BigDecimal? = null,
	@Convert(converter = DangerLevelConverter::class)
	@Column(name = "danger_level")
	var dangerLevel: DangerLevel? = null,
	/**
	* Sizes stored as a JSON array string in the DB (e.g. '["US 6","US 7"]').
	* Parsed to List<String> on read, serialized back to JSON string on write.
	* Only applicable to footwear products, null for all other categories.
	*/
	@Column(name = "sizes", columnDefinition = "JSON")
	var sizesJson: String? = null,
) {
    /**
     * Parsed sizes list derived from the JSON column.
     * Returns null if no sizes are defined for this product.
     */
    val sizes: List<String>?
		get() = sizesJson?.let {
			ObjectMapper().readValue(it, Array<String>::class.java)?.toList()
		}
}