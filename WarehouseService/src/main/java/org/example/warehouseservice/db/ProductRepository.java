package org.example.warehouseservice.db;

import org.example.warehouseservice.db.models.Product;
import org.reactivestreams.Publisher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
    Mono<Product> findByName(String name);
}
