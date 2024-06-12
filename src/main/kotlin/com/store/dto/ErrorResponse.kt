package com.store.dto

data class ErrorResponse(val timestamp: String, val status: Int, val error: String, val path: String)