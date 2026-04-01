package com.lievasoft.repository;

import com.lievasoft.dto.response.ImageCardResponse;
import com.lievasoft.entity.Image;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

import static com.lievasoft.plant.PlantConstant.FETCH_IMAGE_PLANT_CARD_NAME;

@ApplicationScoped
public class ImageRepository implements PanacheRepository<Image> {

    public ImageCardResponse getImagePlantCardOrThrowException(Long plantId) {
        var maybeImageCard = getEntityManager()
                .createNamedQuery(FETCH_IMAGE_PLANT_CARD_NAME, ImageCardResponse.class)
                .setParameter("id", plantId)
                .getSingleResult();

        return Optional.ofNullable(maybeImageCard)
                .orElseThrow(() -> new EntityNotFoundException("Image with plant_id: %s not found".formatted(plantId)));
    }
}
