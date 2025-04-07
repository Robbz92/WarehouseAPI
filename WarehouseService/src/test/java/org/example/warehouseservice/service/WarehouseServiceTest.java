package org.example.warehouseservice.service;

import org.example.warehouseservice.controller.dto.ProductAvailabilityDto;
import org.example.warehouseservice.db.ArticleRepository;
import org.example.warehouseservice.db.ProductArticleRepository;
import org.example.warehouseservice.db.ProductRepository;
import org.example.warehouseservice.db.models.Article;
import org.example.warehouseservice.db.models.Product;
import org.example.warehouseservice.db.models.ProductArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class WarehouseServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductArticleRepository productArticleRepository;

    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        warehouseService = new WarehouseService(articleRepository, productRepository, productArticleRepository);
    }

    @Test
    void getAvailableProducts() {
        var expected = ProductAvailabilityDto.builder()
                .productName("Dinner table")
                .productId(1)
                .availableProductCount(2)
                .build();
        when(productRepository.findAll())
                .thenReturn(Flux.just(Product.builder()
                                .id(1)
                                .price(20)
                                .name("Dinner table")
                        .build()));

        when(productArticleRepository.findByProductId(anyInt()))
                .thenReturn(Flux.just(ProductArticle.builder()
                        .productId(1)
                        .articleId(2)
                        .quantity(1)
                        .build()));

        when(articleRepository.findByArticleId(anyInt()))
                .thenReturn(Flux.just(Article.builder()
                                .availableStock(2)
                                .articleId(2)
                                .name("Dinner table")
                        .build()));

        var response = warehouseService.getAvailableProducts();
        StepVerifier.create(response)
                .expectNext(expected)
                .verifyComplete();

        verify(productRepository, times(1)).findAll();
        verify(productArticleRepository, times(1)).findByProductId(anyInt());
        verify(articleRepository, times(1)).findByArticleId(anyInt());
    }

    @Test
    void should_sellProduct() {
        when(productArticleRepository.findByProductId(anyInt()))
                .thenReturn(Flux.just(ProductArticle.builder()
                                .productId(1)
                                .articleId(2)
                                .quantity(2)
                        .build()));

        when(articleRepository.updateAvailableStock(anyInt(), anyInt()))
                .thenReturn(Mono.empty());

        when(productArticleRepository.deleteByProductId(anyInt()))
                .thenReturn(Mono.empty());

        when(productRepository.deleteById(anyInt()))
                .thenReturn(Mono.empty());

        var response = warehouseService.sellProduct(1, 2);
        StepVerifier.create(response)
                .verifyComplete();

        verify(productArticleRepository, times(1)).findByProductId(anyInt());
        verify(articleRepository, times(1)).updateAvailableStock(anyInt(), anyInt());
        verify(productArticleRepository, times(1)).deleteByProductId(anyInt());
        verify(productRepository, times(1)).deleteById(anyInt());
    }
}