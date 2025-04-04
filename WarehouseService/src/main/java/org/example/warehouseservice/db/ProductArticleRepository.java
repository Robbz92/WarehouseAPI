package org.example.warehouseservice.db;

import org.example.warehouseservice.db.models.ProductArticle;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductArticleRepository extends ReactiveCrudRepository<ProductArticle, Integer> {
}
