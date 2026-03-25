package dev.bitstachio.interdimensional_bazaar.user.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class User(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	var id: UUID? = null,
	var name: String = "",
	var email: String = "",
)
