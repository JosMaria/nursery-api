package com.lievasoft.dto.request.plant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TaxonomyCreateDTO(
        String kingdom,
        String division,

        @JsonProperty("class")
        String clazz,

        String order,
        String family,
        String genus,
        String species
) {
}
