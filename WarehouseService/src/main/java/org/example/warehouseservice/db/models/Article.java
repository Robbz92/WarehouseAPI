package org.example.warehouseservice.db.models;

import lombok.Builder;
import org.example.warehouseservice.service.dto.inventory.InventoryWrapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Table("article")
public record Article(
        @Id
        int id,
        int articleId,
        String name,
        int availableStock) {

        public static List<Article> from(InventoryWrapper inventoryWrapper) {
                return inventoryWrapper.inventory().stream()
                        .map(item -> Article.builder()
                                .articleId(Integer.parseInt(item.art_id()))
                                .name(item.name())
                                .availableStock(Integer.parseInt(item.stock()))
                                .build())
                        .collect(Collectors.toList());
        }

        public Article cloneWith(int quantity){
                return Article.builder()
                        .articleId(id)
                        .name(name)
                        .availableStock(quantity)
                        .build();
        }
}
