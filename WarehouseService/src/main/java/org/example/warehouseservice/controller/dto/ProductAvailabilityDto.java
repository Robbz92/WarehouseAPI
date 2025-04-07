package org.example.warehouseservice.controller.dto;

import lombok.Builder;

@Builder
public record ProductAvailabilityDto(
        String productName,
        int productId,
        int availableProductCount) {  // This will hold the number of complete products

    public static ProductAvailabilityDto of(
            String productName,
            int productId,
            int availableProductCount) {

        return ProductAvailabilityDto.builder()
                .productId(productId)
                .productName(productName)
                .availableProductCount(availableProductCount)
                .build();
    }
}
