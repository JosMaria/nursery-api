package com.lievasoft.dto.plant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageToPlantDetailsDTO(
        String filename,
        @JsonProperty("storage_path")
        String storagePath
) {
}
