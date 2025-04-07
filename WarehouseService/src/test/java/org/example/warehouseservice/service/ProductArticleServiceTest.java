package org.example.warehouseservice.service;

import org.example.warehouseservice.db.ProductArticleRepository;
import org.example.warehouseservice.db.models.Product;
import org.example.warehouseservice.db.models.ProductArticle;
import org.example.warehouseservice.service.dto.products.ArticleAmount;
import org.example.warehouseservice.service.dto.products.ProductItem;
import org.example.warehouseservice.service.dto.products.ProductWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class ProductArticleServiceTest {
    @Mock
    private ProductArticleRepository productArticleRepository;

    private ProductArticleService productArticleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productArticleService = new ProductArticleService(productArticleRepository);
    }

    @Test
    void should_save_product_articles() {
        var product = new Product(1, "Dining Chair", 150.0);
        var article1 = new ArticleAmount("1", 4);
        var article2 = new ArticleAmount("2", 8);
        var item = new ProductItem("Dining Chair", List.of(article1, article2), 150.0);

        var wrapper = new ProductWrapper(List.of(item));

        var expectedProductArticle1 = new ProductArticle(1, 1, 4);
        var expectedProductArticle2 = new ProductArticle(1, 2, 8);

        when(productArticleRepository.save(any(ProductArticle.class)))
                .thenReturn(Mono.just(expectedProductArticle1));
        when(productArticleRepository.save(any(ProductArticle.class)))
                .thenReturn(Mono.just(expectedProductArticle2));

        Mono<Void> result = productArticleService.saveProductArticlesForProduct(product, wrapper);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productArticleRepository, times(2)).save(any(ProductArticle.class));

        verify(productArticleRepository).save(argThat(productArticle ->
                productArticle.productId() == 1 && productArticle.articleId() == 1 && productArticle.quantity() == 4));
        verify(productArticleRepository).save(argThat(productArticle ->
                productArticle.productId() == 1 && productArticle.articleId() == 2 && productArticle.quantity() == 8));
    }
}