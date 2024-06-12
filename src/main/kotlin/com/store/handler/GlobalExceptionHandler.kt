package com.store.handler

import com.store.dto.ApiResponse
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
        ): ResponseEntity<ErrorResponse> {
            val errorResponse = ErrorResponse(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                status = HttpStatus.BAD_REQUEST.value(),
                error = ex.message ?: "Validation error",
                path = request.getDescription(false).substring(4) // Removing 'uri=' prefix
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        @ExceptionHandler(InvalidProductTypeException::class)
        fun handleInvalidProductTypeException(
            ex: InvalidProductTypeException,
            request: WebRequest
        ): ResponseEntity<ApiResponse> {
            val errorResponse = ErrorResponse(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                status = HttpStatus.BAD_REQUEST.value(),
                error = ex.message ?: "Invalid product type",
                path = request.getDescription(false).substring(4)
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        // Other exception handlers can be added here
    }
}