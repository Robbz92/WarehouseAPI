package org.example.warehouseservice.service;

import org.example.warehouseservice.db.ProductRepository;
import org.example.warehouseservice.db.models.Product;
import org.example.warehouseservice.service.converter.Converter;
import org.example.warehouseservice.service.dto.products.ArticleAmount;
import org.example.warehouseservice.service.dto.products.ProductItem;
import org.example.warehouseservice.service.dto.products.ProductWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductArticleService productArticleService;

    @Mock
    private Converter converter;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, productArticleService, converter);
    }

    @Test
    void should_save_product() {
        // Arrange
        String json = """
    {
      "products": [
        {
          "name": "Dining Chair",
          "price": 150,
          "contain_articles": [
            { "art_id": "1", "amount_of": "4" },
            { "art_id": "2", "amount_of": "8" }
          ]
        }
      ]
    }
    """;

        var factory = new DefaultDataBufferFactory();
        var dataBuffer = factory.wrap(json.getBytes(StandardCharsets.UTF_8));


        FilePart filePart = mock(FilePart.class);
        when(filePart.content()).thenReturn(Flux.just(dataBuffer));

        var article1 = new ArticleAmount("1", 4);
        var article2 = new ArticleAmount("2", 8);
        var item = new ProductItem("Dining Chair", List.of(article1, article2), 150.0);
        var wrapper = new ProductWrapper(List.of(item));

        when(converter.toClassEntity(any(DataBuffer.class), eq(ProductWrapper.class)))
                .thenReturn(Mono.just(wrapper));

        var product = Product.from(wrapper).get(0);

        when(productRepository.findByName(product.name())).thenReturn(Mono.empty());
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        when(productArticleService.saveProductArticlesForProduct(product, wrapper))
                .thenReturn(Mono.empty());

        Mono<Void> result = productService.saveProduct(filePart);

        StepVerifier.create(result)
                .verifyComplete();

        verify(filePart).content();
        verify(converter).toClassEntity(any(DataBuffer.class), eq(ProductWrapper.class));
        verify(productRepository).findByName(product.name());
        verify(productRepository).save(product);
        verify(productArticleService).saveProductArticlesForProduct(product, wrapper);
    }
}