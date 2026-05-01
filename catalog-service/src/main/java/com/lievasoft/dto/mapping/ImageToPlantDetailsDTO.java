package com.lievasoft.dto.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageToPlantDetailsDTO(
        String filename,
        @JsonProperty("storage_path")
        String storagePath
) {
}
