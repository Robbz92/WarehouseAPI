package org.example.warehouseservice.db.models;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record ProductArticle(
        @Id
        int productId,
        int articleId,
        int quantity) {
}
