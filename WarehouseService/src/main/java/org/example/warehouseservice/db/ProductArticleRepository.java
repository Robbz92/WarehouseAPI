package org.example.warehouseservice.db;

import org.example.warehouseservice.db.models.ProductArticle;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductArticleRepository extends ReactiveCrudRepository<ProductArticle, Integer> {
    Flux<ProductArticle> findByProductId(int id);

    @Query("DELETE FROM product_article WHERE product_id = :productId")
    Mono<Void> deleteByProductId(int productId);
}
