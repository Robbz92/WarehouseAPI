package org.example.warehouseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.controller.dto.ProductAvailabilityDto;
import org.example.warehouseservice.db.models.Product;
import org.example.warehouseservice.service.ArticleService;
import org.example.warehouseservice.service.ProductService;
import org.example.warehouseservice.service.WarehouseService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseRestController {

    private final WarehouseService warehouseService;
    private final ArticleService articleService;
    private final ProductService productService;

    @PostMapping(value = "/upload/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Storing uploaded json file in database")
    public Mono<Void> uploadArticle(@RequestPart("file") FilePart uploadedFile) {
        return articleService.saveArticle(uploadedFile);
    }

    @PostMapping(value = "/upload/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Storing uploaded json file in database")
    public Mono<Void> uploadProduct(@RequestPart("file") FilePart uploadedFile) {
        return productService.saveProduct(uploadedFile);
    }

    @GetMapping
    @Operation(summary = "Get all available products")
    public Flux<ProductAvailabilityDto> getAllProducts() {
        return warehouseService.getAvailableProducts();
    }

    @PostMapping("/{productId}/sell")
    public Mono<Void> sellProduct(@PathVariable int productId, @RequestParam int quantity) {
        return warehouseService.sellProduct(productId, quantity);
    }
}
