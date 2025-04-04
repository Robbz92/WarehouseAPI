package org.example.warehouseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.db.models.Product;
import org.example.warehouseservice.service.WarehouseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1/warehouse")
@RequiredArgsConstructor
public class RestWarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/test/{id}")
    @Operation(summary = "This is a test endpoint")
    public Mono<String> test(@PathVariable int id) {
        return Mono.just("test");
    }
/*
    @GetMapping("/blah/{id}")
    public Mono<Product> test2(@PathVariable int id) {
        return warehouseService.getProduct(id);
    }

 */
}
