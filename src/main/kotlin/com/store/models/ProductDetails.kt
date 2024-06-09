import com.store.validation.ValidString
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ProductDetails(

    @field:ValidString
    @field:NotBlank(message = "Name must not be blank")
    val name: String,

    @field:NotNull(message = "Type must not be null")
    val type: ProductType,

    @field:Min(value = 1, message = "Inventory must be greater than 0")
    val inventory: Int,

    @field:Min(value = 1, message = "Cost must be greater than 0")
    val cost: Number
)