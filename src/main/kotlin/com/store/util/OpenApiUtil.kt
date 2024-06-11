package com.store.OpenApiUtil

import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI



object OpenApiUtil {
    private val openAPI: OpenAPI

    init {
        val result = OpenAPIParser().readLocation("products_api.yaml", null, null)
        openAPI = result.openAPI
    }

    fun getProductTypes(): List<String> {
        val productTypeSchema = openAPI.components.schemas["ProductType"]
        return (productTypeSchema?.enum as? List<String>) ?: emptyList()
    }
}

