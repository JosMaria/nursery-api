package com.lievasoft.dto.response.plant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lievasoft.entity.Plant;

public record PlantResponse(
        Long id,
        @JsonProperty("scientific_name")
        String scientificName
) {

    public PlantResponse(Plant plant) {
        this(plant.getId(), plant.getScientificName());
    }
}
