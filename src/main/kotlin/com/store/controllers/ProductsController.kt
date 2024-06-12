package com.store.controllers

import Product
import ProductDetails
import com.store.OpenApiUtil.OpenApiUtil
import com.store.dto.ApiResponse
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

    private val products = mutableMapOf<Long, Product>()
    private val idCounter = AtomicLong()

    @PostMapping
    fun createProduct(
        @Valid @RequestBody productDetails: ProductDetails,
        bindingResult: BindingResult
    ): ResponseEntity<ApiResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.allErrors.joinToString { it.defaultMessage ?: "Invalid value" }
            throw ProductValidationException(errorMessage)
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
    fun getProducts(@RequestParam(required = false) type: String?): ResponseEntity<List<Product>> {
        val validTypes = OpenApiUtil.getProductTypes()
        if (type != null && type !in validTypes) {
            throw InvalidProductTypeException("Invalid type value: $type")
        }

        val filteredProducts = if (type != null) {
            products.values.filter { it.type.name.equals(type, ignoreCase = true) }
        } else {
            products.values.toList()
        }
        return ResponseEntity.ok(filteredProducts)
    }


}
