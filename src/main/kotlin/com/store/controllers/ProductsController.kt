package com.store.controllers

import Product
import ProductDetails
import com.store.OpenApiUtil.OpenApiUtil
import com.store.models.ApiResponse
import com.store.models.ErrorResponse
import com.store.models.ProductCreatedResponse
import com.store.models.ProductListResponse
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
    ): ResponseEntity<ApiResponse> {
        if (bindingResult.hasErrors()) {
            val errorResponse = ErrorResponse(
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
        val response = ProductCreatedResponse(id)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    //didn't change any here because we are getting different response body, errorResponse is a different object and list of product is different for the same api.
    fun getProducts(@RequestParam(required = false) type: String?): ResponseEntity<Any> {

        val validTypes = OpenApiUtil.getProductTypes()
        if (type != null && type !in validTypes) {
            val errorResponse = ErrorResponse(
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


}
