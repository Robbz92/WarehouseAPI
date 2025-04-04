package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.db.models.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final ProductArticleService productArticleService;
    private final ProductService productService;
    private final ArticleService articleService;

    public Mono<Product> getProduct(int id) {
        return productService.getProduct(id);
    }
}
