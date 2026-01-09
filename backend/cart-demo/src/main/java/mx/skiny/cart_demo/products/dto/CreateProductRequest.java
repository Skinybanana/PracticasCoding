package mx.skiny.cart_demo.products.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
// record = clase inmutable para transportar datos
// Ideal para DTOs: menos código, más seguridad.

public record CreateProductRequest(
        @NotBlank @Size(max = 200) String name,
        @NotNull @DecimalMin(value = "0.0") BigDecimal price,
        @NotNull @Min(0) Integer stock
) {
}
