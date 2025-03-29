package com.dev.ecommerce.kafka.order;

import java.math.BigDecimal;

public record Product(
        Integer productId,
        String name,
        String descripton,
        BigDecimal price,
        Double quantity
) {
}
