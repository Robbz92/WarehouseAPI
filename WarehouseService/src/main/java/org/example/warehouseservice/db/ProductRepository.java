package org.example.warehouseservice.db;

import org.example.warehouseservice.db.models.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
}
