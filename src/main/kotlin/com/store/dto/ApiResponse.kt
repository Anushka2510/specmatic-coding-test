package com.store.dto


sealed class ApiResponse
data class ProductCreatedResponse(val id: Long) : ApiResponse()
data class ErrorResponse(val timestamp: String, val status: Int, val error: String, val path: String) : ApiResponse()
