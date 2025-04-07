package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.db.ArticleRepository;
import org.example.warehouseservice.db.models.Article;
import org.example.warehouseservice.service.converter.Converter;
import org.example.warehouseservice.service.dto.inventory.InventoryWrapper;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final Converter converter;

    public Mono<Void> saveArticle(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> converter.toClassEntity(dataBuffer, InventoryWrapper.class))
                .flatMapMany(wrapper -> Flux.fromIterable(Article.from(wrapper)))
                .flatMap(article -> articleRepository.findByName(article.name())
                                .flatMap(existingArticle -> {
                                    existingArticle.cloneWith(existingArticle.availableStock() + article.availableStock());
                                    return articleRepository.save(existingArticle);
                                })
                                .switchIfEmpty(articleRepository.save(article)))
                .then();
    }
}
