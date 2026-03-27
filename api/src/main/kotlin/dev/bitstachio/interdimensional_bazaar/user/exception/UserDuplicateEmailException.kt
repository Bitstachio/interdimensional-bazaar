package dev.bitstachio.interdimensional_bazaar.user.exception

import dev.bitstachio.interdimensional_bazaar.common.exception.BadRequestException

class UserDuplicateEmailException(email: String) :
	BadRequestException("User with email already exists: $email")
