package com.store.models

import Product

sealed class ApiResponse
data class ProductCreatedResponse(val id: Long) : ApiResponse()
data class ProductListResponse(val products: List<Product>) : ApiResponse()
data class ErrorResponse(val timestamp: String, val status: Int, val error: String, val path: String) : ApiResponse()
