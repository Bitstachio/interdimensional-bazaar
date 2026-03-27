package dev.bitstachio.interdimensional_bazaar.category.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "categories")
class Category(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	var id: UUID? = null,
	@Column(nullable = false, unique = true)
	var name: String = "",
	@Column(nullable = false, unique = true)
	var slug: String = "",
	@Column(columnDefinition = "TEXT")
	var description: String? = null,
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: LocalDateTime = LocalDateTime.now(),
)
