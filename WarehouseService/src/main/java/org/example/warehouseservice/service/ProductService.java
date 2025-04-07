package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.db.ProductRepository;
import org.example.warehouseservice.db.models.Product;
import org.example.warehouseservice.service.converter.Converter;
import org.example.warehouseservice.service.dto.products.ProductWrapper;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductArticleService productArticleService;
    private final Converter converter;

    public Mono<Void> saveProduct(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> converter.toClassEntity(dataBuffer, ProductWrapper.class))
                .flatMap(this::saveProductsAndArticles);
    }

    private Mono<Void> saveProductsAndArticles(ProductWrapper productWrapper) {
        return Flux.fromIterable(Product.from(productWrapper))
                .flatMap(product -> productRepository.findByName(product.name())
                        .switchIfEmpty(productRepository.save(product)))
                .flatMap(savedProduct -> productArticleService.saveProductArticlesForProduct(savedProduct, productWrapper))
                .onErrorMap(e -> new RuntimeException("Error saving products and articles", e))
                .then();
    }
}
