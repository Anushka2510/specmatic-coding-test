package com.store.controllers

import ErrorResponseBody
import Product
import ProductDetails
import ProductType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicLong
import javax.validation.Valid
import org.springframework.validation.BindingResult

@RestController
@RequestMapping("/products")
class ProductsController {

    private val products = mutableMapOf<Long, Product>()
    private val idCounter = AtomicLong()

    @PostMapping
    fun createProduct(
        @Valid @RequestBody productDetails: ProductDetails,
        bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val errorResponse = ErrorResponseBody(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                status = HttpStatus.BAD_REQUEST.value(),
                error = bindingResult.allErrors.joinToString { it.defaultMessage ?: "Invalid value" },
                path = "/products"
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        val id = idCounter.incrementAndGet()
        val product = Product(
            id = id,
            name = productDetails.name,
            type = productDetails.type,
            inventory = productDetails.inventory,
            cost= productDetails.cost
        )
        products[id] = product

        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("id" to id))
    }

    @GetMapping
    fun getProducts(@RequestParam(required = false) type: String?): ResponseEntity<Any> {
        // Check if the type is valid
        if (type != null && !isValidType(type)) {
            val errorResponse = ErrorResponseBody(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Invalid type value",
                path = "/products"
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        // Proceed with filtering products
        val filteredProducts = if (type != null) {
            products.values.filter { it.type.name.equals(type, ignoreCase = true) }
        } else {
            products.values.toList()
        }
        return ResponseEntity.ok(filteredProducts)
    }

    // Function to check if the type parameter is valid
    private fun isValidType(type: String): Boolean {
        return type.equals("gadget", ignoreCase = true) ||
                type.equals("book", ignoreCase = true) ||
                type.equals("food", ignoreCase = true) ||
                type.equals("other", ignoreCase = true)
    }
}
