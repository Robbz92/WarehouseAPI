package org.example.warehouseservice.db.models;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record Product(
        @Id
        int id,
        String name,
        double price) {
}
