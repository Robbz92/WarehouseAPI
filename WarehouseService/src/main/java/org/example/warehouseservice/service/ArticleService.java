package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.warehouseservice.db.ArticleRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
}
