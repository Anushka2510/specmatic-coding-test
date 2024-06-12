import com.fasterxml.jackson.annotation.JsonProperty
import com.store.OpenApiUtil.OpenApiUtil
import com.store.validation.ValidProductType
import com.store.validation.ValidString
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ProductDetails(

    val id: Long,

    @field:ValidString
    @field:NotBlank(message = "Name must not be blank")
    val name: String,

    @field:NotNull(message = "Type must not be null")
    @field:ValidProductType(message = "Invalid product type")
    val type: String,

    @field:Min(value = 1, message = "Inventory must be greater than 0")
    val inventory: Int,

    @field:Min(value = 1, message = "Cost must be 0 or positive")
    val cost: Int

){

    constructor() : this(0,"", OpenApiUtil.getProductTypes().get(0), 0, 0)
}