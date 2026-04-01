package com.lievasoft.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageCardResponse(
        String filename,
        @JsonProperty("storage_path")
        String storagePath,
        @JsonProperty("content_type")
        String contentType
) {
}
