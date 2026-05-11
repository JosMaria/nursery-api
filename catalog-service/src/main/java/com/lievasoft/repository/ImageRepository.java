package com.lievasoft.repository;

import com.lievasoft.dto.response.image.ImageSelectionResponse;
import com.lievasoft.entity.Image;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

import static com.lievasoft.statement.ImageQuery.IMAGE_SELECTION_PER_PLANT;

@ApplicationScoped
public class ImageRepository implements PanacheRepository<Image> {

    private static final Logger LOG = Logger.getLogger(ImageRepository.class);

    public List<ImageSelectionResponse> findImagesPerPlantToSelect(long plantId) {
        return getEntityManager()
                .createNamedQuery(IMAGE_SELECTION_PER_PLANT, ImageSelectionResponse.class)
                .setParameter("plantId", plantId)
                .getResultList();
    }

    public Image obtainOrThrowException(long imageId, long plantId) {
        Map<String, Object> params = Map.of("id", imageId, "plantId", plantId);
        return find("id = :id AND plant.id = :plantId", params)
                .firstResultOptional()
                .orElseThrow(() -> {
                    LOG.infof("Image with ID: %s and its plantId: %s not found", imageId, plantId);
                    return new EntityNotFoundException();
                });
    }

    public List<Long> fetchImageIdsByPlantId(Long plantId) {
        return find("SELECT id FROM Image WHERE plant.id = ?1 ORDER BY isSelected DESC", plantId)
                .project(Long.class)
                .list();
    }

    public Image findImagePlantBy(long plantId, long imageId) {
        Map<String, Object> params = Map.of("plantId", plantId, "imageId", imageId);
        return find("plant.id = :plantId AND id = :imageId", params)
                .firstResultOptional()
                .orElseThrow(() -> new EntityNotFoundException("Image with ID: %s and plantId: %s not found".formatted(imageId, plantId)));
    }

    public boolean existsByPlant(long plantId) {
        var query = """
                SELECT EXISTS (
                    SELECT TRUE
                    FROM images
                    WHERE plant_id = :plantId)
                """;

        return (boolean) getEntityManager()
                .createNativeQuery(query, Boolean.class)
                .setParameter("plantId", plantId)
                .getSingleResult();
    }

    @Transactional
    public int updateIsSelected(long imageId, long plantId) {
        update("isSelected = FALSE WHERE plant.id = ?1 AND isSelected = TRUE", plantId);
        Map<String, Object> params = Map.of("plantId", plantId, "id", imageId);
        return update("isSelected = TRUE WHERE plant.id = :plantId AND id = :id", params);
    }
}
