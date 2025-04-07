package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.controller.dto.ProductAvailabilityDto;
import org.example.warehouseservice.db.ArticleRepository;
import org.example.warehouseservice.db.ProductArticleRepository;
import org.example.warehouseservice.db.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final ProductArticleService productArticleService;
    private final ArticleRepository articleRepository;
    private final ProductRepository productRepository;
    private final ProductArticleRepository productArticleRepository;

    public Flux<ProductAvailabilityDto> getAvailableProducts() {
        return productRepository.findAll()
                .flatMap(productArticleService::getProductAvailabilityDto)
                .groupBy(ProductAvailabilityDto::productId)
                .flatMap(this::getMinAvailableProduct);
    }

    private Mono<ProductAvailabilityDto> getMinAvailableProduct(GroupedFlux<Integer, ProductAvailabilityDto> groupedFlux) {
        return groupedFlux.collectList()
                .flatMap(dtoList -> Mono.just(dtoList.stream()
                        .min(Comparator.comparingInt(ProductAvailabilityDto::availableQuantity))
                        .orElseThrow(() -> new RuntimeException("No DTOs found"))));
    }

    public Mono<Void> sellProduct(int productId, int quantityToSell) {
        return productArticleRepository.findByProductId(productId)
                .flatMap(productArticle -> articleRepository.updateAvailableStock(productArticle.quantity() * quantityToSell, productArticle.productId()))
                .then(productArticleRepository.deleteByProductId(productId))
                .then(productRepository.deleteById(productId));
    }
}
