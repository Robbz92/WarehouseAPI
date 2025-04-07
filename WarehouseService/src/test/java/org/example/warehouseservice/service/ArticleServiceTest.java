package org.example.warehouseservice.service;

import org.example.warehouseservice.db.ArticleRepository;
import org.example.warehouseservice.db.models.Article;
import org.example.warehouseservice.service.converter.Converter;
import org.example.warehouseservice.service.dto.inventory.InventoryItem;
import org.example.warehouseservice.service.dto.inventory.InventoryWrapper;
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

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private Converter converter;

    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        articleService = new ArticleService(articleRepository, converter);
    }

    @Test
    void should_save_article() {
        // Arrange
        var factory = new DefaultDataBufferFactory();
        String json = """
            {
              "inventory": [
                { "art_id": "1", "name": "leg", "stock": "5" }
              ]
            }
        """;
        DataBuffer dataBuffer = factory.wrap(json.getBytes(StandardCharsets.UTF_8));

        FilePart filePart = mock(FilePart.class);
        when(filePart.content()).thenReturn(Flux.just(dataBuffer));

        var item = new InventoryItem("1", "leg", "5");
        var wrapper = new InventoryWrapper(List.of(item));
        var article = Article.builder()
                .articleId(1)
                .name("leg")
                .availableStock(5)
                .build();

        when(converter.toClassEntity(any(DataBuffer.class), eq(InventoryWrapper.class)))
                .thenReturn(Mono.just(wrapper));

        when(articleRepository.findByName("leg")).thenReturn(Mono.empty());
        when(articleRepository.save(article)).thenReturn(Mono.just(article));

        // Act
        Mono<Void> result = articleService.saveArticle(filePart);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(filePart).content();
        verify(converter).toClassEntity(any(), eq(InventoryWrapper.class));
        verify(articleRepository).findByName("leg");
        verify(articleRepository).save(article);
    }
}
