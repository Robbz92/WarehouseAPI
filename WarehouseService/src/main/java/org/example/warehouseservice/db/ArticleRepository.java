package org.example.warehouseservice.db;

import org.example.warehouseservice.db.models.Article;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ReactiveCrudRepository<Article, Integer> {
}
