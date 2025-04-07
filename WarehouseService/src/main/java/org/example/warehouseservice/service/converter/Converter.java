package org.example.warehouseservice.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class Converter {
    private final ObjectMapper objectMapper;

    public <T> Mono<T> toClassEntity(DataBuffer dataBuffer, Class<T> targetType) {
        return extractFileContent(dataBuffer)
                .flatMap(fileContent -> convertJsonToClass(targetType, fileContent))
                .doFinally(signalType -> DataBufferUtils.release(dataBuffer));
    }

    private <T> Mono<T> convertJsonToClass(Class<T> targetType, String fileContent) {
        try {
            return Mono.just(objectMapper.readValue(fileContent, targetType));
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to parse JSON: " + e.getMessage(), e));
        }
    }

    private Mono<String> extractFileContent(DataBuffer dataBuffer) {
        byte[] content = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(content);
        return Mono.just(new String(content, StandardCharsets.UTF_8));
    }
}
