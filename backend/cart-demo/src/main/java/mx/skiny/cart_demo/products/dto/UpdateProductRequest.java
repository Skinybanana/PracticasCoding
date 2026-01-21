package mx.skiny.cart_demo.products.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank @Size(max = 200) String name,
        @NotNull @DecimalMin(value = "0.0") BigDecimal price,
        @NotNull @Min(0) Integer stock,
        @NotNull Boolean active
) { }
