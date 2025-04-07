package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.controller.dto.ProductAvailabilityDto;
import org.example.warehouseservice.db.ArticleRepository;
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
    private final ArticleRepository articleRepository;

    public Flux<ProductAvailabilityDto> getProductAvailabilityDto(Product product) {
        return productArticleRepository.findByProductId(product.id())
                .flatMap(productArticle -> articleRepository.findByArticleId(productArticle.articleId())
                        .map(article -> ProductAvailabilityDto.of(article.articleId(), product.id(), product.name(), productArticle.quantity())))
                .filter(dto -> dto.availableQuantity() > 0);
    }

    public Mono<Void> saveProductArticlesForProduct(Product savedProduct, ProductWrapper productWrapper) {
        return Flux.fromIterable(createProductArticles(savedProduct, productWrapper))
                .doOnNext(productArticle -> {

                    System.out.println("Saving product article: " + productArticle);
                })
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
