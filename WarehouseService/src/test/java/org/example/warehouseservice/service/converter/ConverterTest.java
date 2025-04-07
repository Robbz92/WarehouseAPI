package org.example.warehouseservice.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.warehouseservice.db.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

class ConverterTest {

    private ObjectMapper objectMapper;
    private Converter converter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        converter = new Converter(objectMapper);
    }

    @Test
    void testToClassEntity_success() {
        String jsonContent = "{\"name\":\"Sample Product\",\"price\":100}";
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonContent.getBytes(StandardCharsets.UTF_8));

        Mono<Product> result = converter.toClassEntity(dataBuffer, Product.class);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.name().equals("Sample Product") && product.price() == 100)
                .verifyComplete();
    }

    @Test
    void testToClassEntity_failure() {
        String invalidJsonContent = "{\"name\":\"Sample Product\",\"price\":\"oops\"}";
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(invalidJsonContent.getBytes(StandardCharsets.UTF_8));

        Mono<Product> result = converter.toClassEntity(dataBuffer, Product.class);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().startsWith("Failed to parse JSON:"))
                .verify();
    }
}
