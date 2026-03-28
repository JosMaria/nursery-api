package com.lievasoft.dto.plant;

import com.lievasoft.entity.Image;

public record PlantImageResponse(
        String url,
        String filename,
        long size
) {

    public PlantImageResponse(Image image) {
        this(image.getUrl(), image.getFileName(), image.getFileSize());
    }
}
