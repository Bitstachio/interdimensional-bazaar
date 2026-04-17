package dev.bitstachio.interdimensional_bazaar.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Convert
import java.time.LocalDateTime
import java.util.UUID
import dev.bitstachio.interdimensional_bazaar.user.domain.UserRole

@Entity
@Table(name = "users")
class User(
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	var id: UUID? = null,
	@Column(name = "first_name")
	var firstName: String? = null,
	@Column(name = "last_name")
	var lastName: String? = null,
	@Column(nullable = false, unique = true)
	var email: String = "",
	@Column(name = "password_hash", nullable = false)
	var passwordHash: String = "",
	@Column(name = "created_at", nullable = false, updatable = false)
	var createdAt: LocalDateTime = LocalDateTime.now(),
	@Convert(converter = UserRoleConverter::class)
	@Column(nullable = false)
	var role: UserRole = UserRole.CUSTOMER,
	@Column(name = "phone")
	var phone: String? = null,
)
