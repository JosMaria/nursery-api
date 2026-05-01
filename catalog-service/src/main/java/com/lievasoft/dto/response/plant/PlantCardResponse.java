package com.lievasoft.dto.response.plant;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PlantCardResponse(
        @JsonProperty("id")
        long plantId,

        @JsonProperty("scientific_name")
        String scientificName,

        @JsonProperty("is_favorite")
        boolean isFavorite,

        @JsonProperty("common_name")
        String commonName,

        BigDecimal price,

        @JsonProperty("selected_image_id")
        Long selectedImageId
) {
}
