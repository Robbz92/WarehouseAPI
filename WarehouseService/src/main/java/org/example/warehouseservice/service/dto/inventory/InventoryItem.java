package org.example.warehouseservice.service.dto.inventory;

import lombok.Builder;

@Builder
public record InventoryItem(
        String art_id,
        String name,
        String stock) {
}
