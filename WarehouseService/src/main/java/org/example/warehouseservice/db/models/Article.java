package org.example.warehouseservice.db.models;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record Article(
        @Id
        int id,
        String name,
        boolean availableStock) {
}
