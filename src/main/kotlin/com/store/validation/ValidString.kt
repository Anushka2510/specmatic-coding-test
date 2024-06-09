package com.store.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [StringValidator::class])
annotation class ValidString(
    val message: String = "Invalid value, must be a string",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class StringValidator : ConstraintValidator<ValidString, Any?> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return value is String && value.matches(Regex("^[a-zA-Z]*\$")) && (value!="true") &&( value!="false")
    }
}
