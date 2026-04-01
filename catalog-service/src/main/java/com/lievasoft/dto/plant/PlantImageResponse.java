package com.lievasoft.dto.plant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lievasoft.entity.Image;

public record PlantImageResponse(
        @JsonProperty("storage_path")
        String storagePath,
        String filename,
        long size
) {

    public PlantImageResponse(Image image) {
        this(image.getStoragePath(), image.getFilename(), image.getFileSize());
    }
}
