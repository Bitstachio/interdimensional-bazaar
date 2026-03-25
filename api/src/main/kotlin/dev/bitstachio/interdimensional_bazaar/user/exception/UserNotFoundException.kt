package dev.bitstachio.interdimensional_bazaar.user.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.ResourceNotFoundException
import java.util.UUID

class UserNotFoundException(id: UUID) : ResourceNotFoundException("User not found with ID: $id")
