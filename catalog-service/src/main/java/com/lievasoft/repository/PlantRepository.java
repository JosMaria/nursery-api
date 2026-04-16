package com.lievasoft.repository;

import com.lievasoft.dto.plant.PlantTaxonomy;
import com.lievasoft.dto.response.PlantCardResponse;
import com.lievasoft.entity.Plant;
import com.lievasoft.exception.PlantNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static com.lievasoft.plant.PlantConstant.FETCH_PLANT_CARDS;
import static com.lievasoft.plant.PlantConstant.FETCH_PLANT_TAXONOMY;

@ApplicationScoped
public class PlantRepository implements PanacheRepository<Plant> {

    @Transactional
    public void create(Plant plant) {
        persist(plant);
    }

    public List<PlantCardResponse> fetchPlantCards() {
        return getEntityManager()
                .createNamedQuery(FETCH_PLANT_CARDS, PlantCardResponse.class)
                .getResultList();
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
}
