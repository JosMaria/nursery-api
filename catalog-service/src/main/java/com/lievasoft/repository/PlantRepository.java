package com.lievasoft.repository;

import com.lievasoft.dto.response.plant.PaginatedResult;
import com.lievasoft.dto.mapping.PlantTaxonomy;
import com.lievasoft.dto.response.plant.PlantCardResponse;
import com.lievasoft.entity.Plant;
import com.lievasoft.exception.PlantNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.lievasoft.plant.PlantConstant.FETCH_PLANT_CARDS;
import static com.lievasoft.plant.PlantConstant.FETCH_PLANT_TAXONOMY;

@ApplicationScoped
public class PlantRepository implements PanacheRepository<Plant> {

    @Transactional
    public void create(Plant plant) {
        persist(plant);
    }

    public PaginatedResult<PlantCardResponse> fetchPaginatedPlantCards(int numberPage, int size) {
        long totalOfPlants = count();
        List<PlantCardResponse> plantCards = getEntityManager()
                .createNamedQuery(FETCH_PLANT_CARDS, PlantCardResponse.class)
                .setParameter("limit", size)
                .setParameter("offset", size * numberPage)
                .getResultList();

        int totalPages = (int) Math.ceil((double) totalOfPlants / size);
        return new PaginatedResult<>(plantCards, numberPage, size, count(), totalPages);
    }

    public Plant obtainByIdOrThrowException(Long plantId) {
        return this.findByIdOptional(plantId).orElseThrow(() -> new PlantNotFoundException(plantId));
    }

    public PlantTaxonomy fetchPlantTaxonomyById(Long plantId) {
        var plantTaxonomy = getEntityManager()
                .createNamedQuery(FETCH_PLANT_TAXONOMY, PlantTaxonomy.class)
                .setParameter("id", plantId)
                .getSingleResult();

        return Optional.ofNullable(plantTaxonomy)
                .orElseThrow(() -> new PlantNotFoundException(plantId));
    }

    public int countOfFavorites() {
        return (int) count("isFavorite = TRUE");
    }

    @Transactional
    public int updateIsFavorite(Long plantId, boolean isFavorite) {
        Map<String, Object> params = Map.of("isFavorite", isFavorite, "id", plantId);
        return update("isFavorite = :isFavorite WHERE id = :id", params);
    }
}
