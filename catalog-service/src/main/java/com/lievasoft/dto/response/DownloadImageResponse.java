package com.lievasoft.dto.response;

public record DownloadImageResponse(
        byte[] imageBytes,
        String contentType
) {
}
