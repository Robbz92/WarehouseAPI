package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.db.ProductArticleRepository;
import org.example.warehouseservice.db.models.Product;
import org.example.warehouseservice.db.models.ProductArticle;
import org.example.warehouseservice.service.dto.products.ProductWrapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductArticleService {
    private final ProductArticleRepository productArticleRepository;

    public Mono<Void> saveProductArticlesForProduct(Product savedProduct, ProductWrapper productWrapper) {
        return Flux.fromIterable(createProductArticles(savedProduct, productWrapper))
                .flatMap(productArticleRepository::save)
                .then()
                .onErrorMap(e -> new RuntimeException("Error saving product articles", e));
    }

    private List<ProductArticle> createProductArticles(Product savedProduct, ProductWrapper productWrapper) {
        return productWrapper.products().stream()
                .filter(productItem -> productItem.name().equals(savedProduct.name()))
                .flatMap(productItem -> productItem.containArticles().stream())
                .map(articleAmount -> ProductArticle.builder()
                        .productId(savedProduct.id())
                        .articleId(Integer.parseInt(articleAmount.artId()))
                        .quantity(articleAmount.amountOf())
                        .build())
                .collect(Collectors.toList());
    }
}
