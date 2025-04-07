package org.example.warehouseservice.controller.dto;

import lombok.Builder;

@Builder
public record ProductAvailabilityDto(
        String productName,
        int productId,
        int articleId,
        int availableQuantity) {

    public static ProductAvailabilityDto of(
            int articleId,
            int productId,
            String productName,
            int availableQuantity) {

        return ProductAvailabilityDto.builder()
                .productId(productId)
                .productName(productName)
                .articleId(articleId)
                .availableQuantity(availableQuantity)
                .build();
    }
}
