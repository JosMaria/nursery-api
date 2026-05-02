package com.lievasoft.dto.response.plant;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record PlantSummaryResponse(
        Long id,

        @JsonProperty("scientific_name")
        String scientificName,

        @JsonProperty("is_favorite")
        @ProjectedFieldName("isFavorite")
        boolean favorite
) {
}
