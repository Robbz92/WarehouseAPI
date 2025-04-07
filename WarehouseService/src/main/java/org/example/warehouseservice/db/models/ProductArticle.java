package org.example.warehouseservice.db.models;

import lombok.Builder;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("product_article")
public record ProductArticle(
        int productId,
        int articleId,
        int quantity) {
}
