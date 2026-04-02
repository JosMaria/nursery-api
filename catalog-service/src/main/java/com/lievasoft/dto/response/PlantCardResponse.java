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
        @JsonProperty("storage_path_image")
        String storagePathImage,
        @JsonProperty("filename_image")
        String filenameImage
) {
}
