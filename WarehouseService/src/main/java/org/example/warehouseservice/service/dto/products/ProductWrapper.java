package org.example.warehouseservice.service.dto.products;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductWrapper(List<ProductItem> products) {
}
