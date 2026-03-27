package dev.bitstachio.interdimensional_bazaar.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException::class)
	fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
		val response =
			ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.message ?: "", LocalDateTime.now())
		return ResponseEntity(response, HttpStatus.NOT_FOUND)
	}

	@ExceptionHandler(BadRequestException::class)
	fun handleBadRequest(ex: BadRequestException): ResponseEntity<ErrorResponse> {
		val response =
			ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message ?: "", LocalDateTime.now())
		return ResponseEntity(response, HttpStatus.BAD_REQUEST)
	}

	@ExceptionHandler(Exception::class)
	fun handleGlobalException(): ResponseEntity<ErrorResponse> {
		val response =
			ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"An unexpected error occurred",
				LocalDateTime.now(),
			)
		return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
	}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidationExceptions(
		ex: MethodArgumentNotValidException,
	): ResponseEntity<Map<String, String>> {
		val errors = HashMap<String, String>()
		ex.bindingResult.fieldErrors.forEach { error ->
			errors[error.field] = error.defaultMessage ?: ""
		}
		return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
	}
}
