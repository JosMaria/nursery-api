package com.lievasoft.dto.response.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageSelectionResponse(
        @JsonProperty("image_id")
        Long imageId,
        String filename,
        @JsonProperty("is_selected")
        boolean isSelected
) {
}
