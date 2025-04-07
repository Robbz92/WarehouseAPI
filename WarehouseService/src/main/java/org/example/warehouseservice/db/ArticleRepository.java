package org.example.warehouseservice.db;

import org.example.warehouseservice.db.models.Article;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ArticleRepository extends ReactiveCrudRepository<Article, Integer> {
    Mono<Article> findByName(String name);
    @Query("UPDATE article SET available_stock = available_stock - :quantity WHERE article_id = :articleId")
    Mono<Void> updateAvailableStock(int quantity, int articleId);

    Flux<Article> findByArticleId(int articleId);
}
