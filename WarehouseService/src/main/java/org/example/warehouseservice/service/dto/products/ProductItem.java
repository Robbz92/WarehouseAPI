package org.example.warehouseservice.service.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductItem(
        String name,
        @JsonProperty("contain_articles")
        List<ArticleAmount> containArticles,
        double price) {
}
