package com.store.validation

import com.store.OpenApiUtil.OpenApiUtil
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidProductTypeValidator::class])
annotation class ValidProductType(
    val message: String = "Invalid product type",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ValidProductTypeValidator : ConstraintValidator<ValidProductType, String> {
    private val validTypes: Set<String> = OpenApiUtil.getProductTypes().toSet()

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value != null && validTypes.contains(value)
    }
}
