package org.example.warehouseservice.controller.dto;

import lombok.Builder;

@Builder
public record ProductArticleInfo(
        int articleId,
        int requiredAmount,
        int availableStock) {

    public static ProductArticleInfo from(int articleId, int requiredAmount, int availableStock) {
        return new ProductArticleInfo(articleId, requiredAmount, availableStock);
    }
}
