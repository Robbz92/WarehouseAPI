package org.example.warehouseservice.db.models;

import lombok.Builder;
import org.example.warehouseservice.service.dto.products.ProductWrapper;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record Product(
        @Id
        int id,
        String name,
        double price) {

        public static List<Product> from(ProductWrapper productWrapper) {
                return productWrapper.products().stream()
                        .map(productItem -> Product.builder()
                                .name(productItem.name())
                                .price(productItem.price())
                                .build())
                        .collect(Collectors.toList());
        }
}
