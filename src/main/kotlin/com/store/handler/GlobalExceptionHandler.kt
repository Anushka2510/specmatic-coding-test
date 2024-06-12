package com.store.handler

import com.store.dto.ErrorResponse
import com.store.exception.InvalidProductTypeException
import com.store.exception.ProductValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ControllerAdvice
class GlobalExceptionHandler {

    @ControllerAdvice
    class GlobalExceptionHandler {

        @ExceptionHandler(ProductValidationException::class)
        fun handleProductValidationException(
            ex: ProductValidationException,
            request: WebRequest
        ):ResponseEntity<ErrorResponse> {
            return buildErrorResponse(ex.message ?: "Validation error", request, HttpStatus.BAD_REQUEST)
        }


        @ExceptionHandler(InvalidProductTypeException::class)
        fun handleInvalidProductTypeException(
            ex: InvalidProductTypeException,
            request: WebRequest
        ): ResponseEntity<ErrorResponse> {
            return buildErrorResponse(ex.message ?: "Invalid product type", request, HttpStatus.BAD_REQUEST)
        }

        private fun buildErrorResponse(
            errorMessage: String,
            request: WebRequest,
            status: HttpStatus
        ): ResponseEntity<ErrorResponse> {
            val errorResponse = ErrorResponse(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                status = status.value(),
                error = errorMessage,
                path = request.getDescription(false).substring(4) // Removing 'uri=' prefix
            )
            return ResponseEntity.status(status).body(errorResponse)
        }

        // Other exception handlers can be added here
    }
}