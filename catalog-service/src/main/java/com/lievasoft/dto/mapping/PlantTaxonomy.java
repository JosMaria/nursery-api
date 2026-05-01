package com.lievasoft.dto.mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlantTaxonomy(
        Long id,
        String scientificName,
        BigDecimal price,
        LocalDateTime updatedAt,
        String division,
        String taxonomyClass,
        String order,
        String family,
        String genus,
        String species
) {
}
