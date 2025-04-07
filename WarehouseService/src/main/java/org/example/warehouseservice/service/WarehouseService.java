package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.controller.dto.ProductAvailabilityDto;
import org.example.warehouseservice.db.ArticleRepository;
import org.example.warehouseservice.db.ProductArticleRepository;
import org.example.warehouseservice.db.ProductRepository;
import org.example.warehouseservice.db.models.Article;
import org.example.warehouseservice.db.models.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

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
                .flatMap(this::calculateAvailableProductCount)  // Calculate for each product
                .filter(dto -> dto.availableProductCount() > 0);  // Only return products that have availability
    }
    // This method calculates the available quantity of products
    private Mono<ProductAvailabilityDto> calculateAvailableProductCount(Product product) {
        return productArticleRepository.findByProductId(product.id())
                .flatMap(productArticle -> articleRepository.findByArticleId(productArticle.articleId())
                        .map(article -> new Object[] { article.articleId(), productArticle.quantity(), article.availableStock() })) // Retrieve article info (ID, required amount, stock)
                .collectList() // Collect all article info for the product
                .map(articleInfos -> {
                    int minProductCount = Integer.MAX_VALUE;

                    // For each article required for the product, calculate how many complete products can be made
                    for (Object[] articleInfo : articleInfos) {
                        int articleId = (int) articleInfo[0];
                        int requiredAmount = (int) articleInfo[1];
                        int stock = (int) articleInfo[2];

                        // Calculate how many products can be made with this article's stock
                        int availableProductCountForArticle = stock / requiredAmount;

                        // Get the minimum number of products from all articles
                        minProductCount = Math.min(minProductCount, availableProductCountForArticle);
                    }

                    // Return a DTO with the result for this product
                    return ProductAvailabilityDto.of(product.name(), product.id(), minProductCount);
                });
    }

    public Mono<Void> sellProduct(int productId, int quantityToSell) {
        return productArticleRepository.findByProductId(productId)
                .flatMap(productArticle -> articleRepository.updateAvailableStock(productArticle.quantity() * quantityToSell, productArticle.productId()))
                .then(productArticleRepository.deleteByProductId(productId))
                .then(productRepository.deleteById(productId));
    }
}
