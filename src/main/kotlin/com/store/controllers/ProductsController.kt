package com.store.controllers

import ProductDetails
import com.store.OpenApiUtil.OpenApiUtil
import com.store.dto.ProductCreatedResponse
import com.store.exception.InvalidProductTypeException
import com.store.exception.ProductValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong
import javax.validation.Valid

@RestController
@RequestMapping("/products")
class ProductsController {

    private val products = mutableMapOf<Long, ProductDetails>()
    private val idCounter = AtomicLong()

    @PostMapping
    fun createProduct(
        @Valid @RequestBody productDetails: ProductDetails,
        bindingResult: BindingResult
    ): ResponseEntity<ProductCreatedResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.allErrors.joinToString { it.defaultMessage ?: "Invalid value" }
            throw ProductValidationException(errorMessage)
        }

        val id = idCounter.incrementAndGet()
        products[id] = productDetails
        val response = ProductCreatedResponse(id)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getProducts(@RequestParam(required = false) type: String?): ResponseEntity<List<ProductDetails>> {
        val validTypes = OpenApiUtil.getProductTypes()
        if (type != null && type !in validTypes) {
            throw InvalidProductTypeException("Invalid type value: $type")
        }

        val filteredProducts = if (type != null) {
            products.values.filter { it.type.equals(type, ignoreCase = true) }
        } else {
            products.values.toList()
        }
        return ResponseEntity.ok(filteredProducts)
    }


}
