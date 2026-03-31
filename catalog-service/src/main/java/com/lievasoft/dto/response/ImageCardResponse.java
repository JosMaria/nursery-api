package com.lievasoft.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageCardResponse(
        String url,
        @JsonProperty("content_type")
        String contentType
) {
}
