package com.lievasoft.dto.request.plant;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Collection;

public record PlantCreateDTO(
        @JsonProperty("scientific_name")
        String scientificName,

        @JsonProperty("common_names")
        Collection<CommonNameCreateDTO> commonNamesDTO,

        @JsonProperty("taxonomy")
        TaxonomyCreateDTO taxonomyDTO,

        BigDecimal price
) {
}
