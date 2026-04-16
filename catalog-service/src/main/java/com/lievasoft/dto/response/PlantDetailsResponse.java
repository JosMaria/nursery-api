package com.lievasoft.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lievasoft.dto.plant.CommonNameToPlantDetails;
import com.lievasoft.dto.plant.ImageToPlantDetailsDTO;
import com.lievasoft.dto.plant.PlantTaxonomy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PlantDetailsResponse(
        Long id,
        @JsonProperty("scientific_name")
        String scientificName,
        BigDecimal price,
        @JsonProperty("updated_at")
        LocalDateTime updatedAt,
        TaxonomyResponse taxonomy,
        @JsonProperty("images_info")
        List<ImageToPlantDetailsDTO> imagesToPlantDetailsDTO,
        @JsonProperty("common_names")
        List<CommonNameToPlantDetails> commonNames
) {

    public PlantDetailsResponse(PlantTaxonomy plantTaxonomy,
                                List<ImageToPlantDetailsDTO> imagesToPlantDetailsDTO,
                                List<CommonNameToPlantDetails> commonNames) {
        this(
                plantTaxonomy.id(),
                plantTaxonomy.scientificName(),
                plantTaxonomy.price(),
                plantTaxonomy.updatedAt(),
                new TaxonomyResponse(
                        plantTaxonomy.division(),
                        plantTaxonomy.taxonomyClass(),
                        plantTaxonomy.order(),
                        plantTaxonomy.family(),
                        plantTaxonomy.genus(),
                        plantTaxonomy.species()
                ),
                imagesToPlantDetailsDTO,
                commonNames
        );
    }

    record TaxonomyResponse(
            String division,
            @JsonProperty("class")
            String taxonomyClass,
            String order,
            String family,
            String genus,
            String species
    ) {
    }
/*
    public PlantDetailsResponse(Map<String, String> redisPlantHash) {
        this(
                Long.parseLong(redisPlantHash.get("id")),
                redisPlantHash.get("commonName"),
                redisPlantHash.get("scientificName"),
                LocalDateTime.parse(redisPlantHash.get("updatedAt"))
        );
    }

    public Map<String, String> mapToRedisHash() {
        return Map.of(
                "id", String.valueOf(id),
                "commonName", commonName,
                "scientificName", scientificName,
                "updatedAt", updatedAt.toString()
        );
    }*/
}
