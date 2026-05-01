package com.lievasoft.dto.response.plant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlantSummaryResponse(
        Long id,
        @JsonProperty("scientific_name")
        String scientificName,
        @JsonProperty("is_favorite")
        boolean isFavorite
) {
}
