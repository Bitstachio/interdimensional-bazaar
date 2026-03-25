package dev.bitstachio.interdimensional_bazaar.user.repository

import dev.bitstachio.interdimensional_bazaar.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID>
