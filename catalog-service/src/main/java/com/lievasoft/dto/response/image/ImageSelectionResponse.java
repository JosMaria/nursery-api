package com.lievasoft.dto.response.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageSelectionResponse(
        @JsonProperty("image_id")
        Long imageId,
        @JsonProperty("is_selected")
        boolean isSelected,
        // TODO: I will delete this property in the frontend because it's not necessary, for now this is a demo
        @JsonProperty("plant_id")
        Long plantId
) {
}
