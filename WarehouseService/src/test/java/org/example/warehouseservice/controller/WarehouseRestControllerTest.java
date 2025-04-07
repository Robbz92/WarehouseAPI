package org.example.warehouseservice.controller;

import org.example.warehouseservice.controller.dto.ProductAvailabilityDto;
import org.example.warehouseservice.service.ArticleService;
import org.example.warehouseservice.service.ProductService;
import org.example.warehouseservice.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WarehouseRestControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private ArticleService articleService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private WarehouseRestController warehouseRestController;

    @Test
    void testGetAllProducts() {
        when(warehouseService.getAvailableProducts())
                .thenReturn(Flux.just(ProductAvailabilityDto.builder().build()));

        WebTestClient
                .bindToController(warehouseRestController)
                .build()
                .get()
                .uri("/v1/warehouse")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductAvailabilityDto.class)
                .consumeWith(response ->
                        assertNotNull(response.getResponseBody()));
    }
    @Test
    void testUploadArticle() {
        when(articleService.saveArticle(any()))
                .thenReturn(Mono.empty());

        WebTestClient
                .bindToController(warehouseRestController)
                .build()
                .post()
                .uri("/v1/warehouse/upload/article")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(generateMultipart("sample-article.json"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .isEmpty();;
    }

    @Test
    void testUploadProduct() {
        when(productService.saveProduct(any()))
                .thenReturn(Mono.empty());

        WebTestClient.bindToController(warehouseRestController)
                .build()
                .post()
                .uri("/v1/warehouse/upload/product")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(generateMultipart("sample-product.json"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .isEmpty();;
    }

    @Test
    void testSellProduct() {
        int productId = 151;
        int quantity = 1;

        when(warehouseService.sellProduct(eq(productId), eq(quantity)))
                .thenReturn(Mono.empty());

        WebTestClient
                .bindToController(warehouseRestController)
                .build()
                .post()
                .uri("/v1/warehouse/{productId}/sell?quantity={quantity}", productId, quantity)
                .exchange()
                .expectStatus().isOk();
    }

    private org.springframework.util.MultiValueMap<String, HttpEntity<?>> generateMultipart(String fileName) {
        var builder = new org.springframework.http.client.MultipartBodyBuilder();
        builder.part("file", new ClassPathResource(fileName));
        return builder.build();
    }
}