package com.lievasoft.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PlantCardResponse(
        @JsonProperty("id")
        long plantId,
        @JsonProperty("scientific_name")
        String scientificName,
        @JsonProperty("common_name")
        String commonName,
        BigDecimal price,
        String url
) {
}
