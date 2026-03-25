package dev.bitstachio.interdimensional_bazaar.common.exception

import java.time.LocalDateTime

data class ErrorResponse(val status: Int, val message: String, val timestamp: LocalDateTime)
