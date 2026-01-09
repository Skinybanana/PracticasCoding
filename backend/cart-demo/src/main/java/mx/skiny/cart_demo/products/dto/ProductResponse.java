package mx.skiny.cart_demo.products.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer stock,
        Boolean active
) {
}
