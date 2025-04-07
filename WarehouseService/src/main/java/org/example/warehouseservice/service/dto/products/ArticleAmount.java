package org.example.warehouseservice.service.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ArticleAmount(
        @JsonProperty("art_id")
        String artId,
        @JsonProperty("amount_of")
        int amountOf) {
}
