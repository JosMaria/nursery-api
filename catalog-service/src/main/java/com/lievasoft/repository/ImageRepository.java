package com.lievasoft.repository;

import com.lievasoft.dto.plant.ImageToPlantDetailsDTO;
import com.lievasoft.dto.response.ImageCardResponse;
import com.lievasoft.entity.Image;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.lievasoft.plant.PlantConstant.FETCH_IMAGE_PLANT_CARD;
import static com.lievasoft.plant.PlantConstant.FETCH_IMAGE_PLANT_CARDS;

@ApplicationScoped
public class ImageRepository implements PanacheRepository<Image> {

    public ImageCardResponse fetchImagePlantCardOrThrowException(Long plantId) {
        var maybeImageCard = getEntityManager()
                .createNamedQuery(FETCH_IMAGE_PLANT_CARD, ImageCardResponse.class)
                .setParameter("id", plantId)
                .getSingleResult();

        return Optional.ofNullable(maybeImageCard)
                .orElseThrow(() -> new EntityNotFoundException("Image with plant_id: %s not found".formatted(plantId)));
    }

    public List<ImageToPlantDetailsDTO> fetchImageUrlsByPlantId(Long plantId) {
        return getEntityManager()
                .createNamedQuery(FETCH_IMAGE_PLANT_CARDS, ImageToPlantDetailsDTO.class)
                .setParameter("id", plantId)
                .getResultList();
    }

    public Image findImagePlantBy(Long plantId, String filename) {
        Map<String, Object> params = Map.of("plantId", plantId, "filename", filename);
        return find("plant.id = :plantId AND filename = :filename", params)
                .firstResultOptional()
                .orElseThrow(() -> new EntityNotFoundException("Image with not found"));
    }
}
