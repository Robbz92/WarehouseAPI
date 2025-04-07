package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.controller.dto.ProductArticleInfo;
import org.example.warehouseservice.controller.dto.ProductAvailabilityDto;
import org.example.warehouseservice.db.ArticleRepository;
import org.example.warehouseservice.db.ProductArticleRepository;
import org.example.warehouseservice.db.ProductRepository;
import org.example.warehouseservice.db.models.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final ArticleRepository articleRepository;
    private final ProductRepository productRepository;
    private final ProductArticleRepository productArticleRepository;

    public Flux<ProductAvailabilityDto> getAvailableProducts() {
        return productRepository.findAll()
                .flatMap(this::calculateAvailableProductCount)
                .filter(dto -> dto.availableProductCount() > 0);
    }

    private Mono<ProductAvailabilityDto> calculateAvailableProductCount(Product product) {
        return productArticleRepository.findByProductId(product.id())
                .flatMap(productArticle -> articleRepository.findByArticleId(productArticle.articleId())
                        .map(article -> ProductArticleInfo.from(article.articleId(), productArticle.quantity(), article.availableStock())))
                .collectList()
                .map(articleInfos -> getProductAvailabilityDto(product, articleInfos));
    }

    private static ProductAvailabilityDto getProductAvailabilityDto(Product product, List<ProductArticleInfo> articleInfos) {
        int minProductCount = articleInfos.stream()
                .mapToInt(articleInfo -> articleInfo.availableStock() / articleInfo.requiredAmount())
                .min()
                .orElse(Integer.MAX_VALUE);

        return ProductAvailabilityDto.of(product.name(), product.id(), minProductCount);
    }

    public Mono<Void> sellProduct(int productId, int quantityToSell) {
        return productArticleRepository.findByProductId(productId)
                .flatMap(productArticle -> articleRepository.updateAvailableStock(productArticle.quantity() * quantityToSell, productArticle.productId()))
                .then(productArticleRepository.deleteByProductId(productId))
                .then(productRepository.deleteById(productId));
    }
}
