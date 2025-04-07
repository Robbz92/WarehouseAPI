package org.example.warehouseservice.service.dto.inventory;

import lombok.Builder;
import java.util.List;

@Builder
public record InventoryWrapper(List<InventoryItem> inventory) {
}
