package com.lievasoft.dto.response.image;

public record DownloadImageResponse(
        byte[] imageBytes,
        String contentType
) {
}
