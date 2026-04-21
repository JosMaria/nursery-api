package com.lievasoft.service;

import com.lievasoft.dto.plant.PlantImageResponse;
import com.lievasoft.dto.response.DownloadImageResponse;
import com.lievasoft.dto.response.image.ImageSelectionResponse;
import com.lievasoft.entity.Image;
import com.lievasoft.repository.ImageRepository;
import com.lievasoft.repository.PlantRepository;
import com.lievasoft.service.storage.ImageStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.update;

@ApplicationScoped
public class DefaultImageService implements ImageService {

    private static final Logger LOG = Logger.getLogger(DefaultImageService.class);

    private final ImageStorageService imageStorageService;
    private final PlantRepository plantRepository;
    private final ImageRepository imageRepository;

    public DefaultImageService(ImageStorageService imageStorageService,
                               PlantRepository plantRepository,
                               ImageRepository imageRepository) {
        this.imageStorageService = imageStorageService;
        this.plantRepository = plantRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<ImageSelectionResponse> obtainImagesSummaryToSelect(final Long plantId) {
        LOG.infof("Obtaining image summary to selection from plantId: %s", plantId);
        return imageRepository.findImagesPerPlantToSelect(plantId);
    }

    @Override
    public DownloadImageResponse obtainImagePlantBy(long plantId, long imageId) {
        var obtainedImage = imageRepository.findImagePlantBy(plantId, imageId);
        LOG.infof("Downloading image with ID: %s and plantId: %s", imageId, plantId);
        byte[] imageBytes = imageStorageService.downloadImageFromFileSystem(
                obtainedImage.getFilename(), obtainedImage.getStoragePath());
        return new DownloadImageResponse(imageBytes, obtainedImage.getContentType());
    }

    @Override
    public boolean setImageAsSelected(long plantId, long imageId) {
        var obtainedImage = imageRepository.obtainOrThrowException(imageId, plantId);
        boolean isSelected = obtainedImage.getSelected() != null ? obtainedImage.getSelected() : false;
        if (!isSelected) {
            int affectedRows = imageRepository.updateIsSelected(imageId, plantId);
            LOG.infof("Image with id: %s and plantId: %s was update its property isSelected", imageId, plantId);
            return affectedRows == 1;

        } else return false;
    }

    @Transactional
    public PlantImageResponse persist(long plantId, boolean isSelected, FileUpload imageUpload) {
        var persistedPlant = plantRepository.obtainByIdOrThrowException(plantId);
        var uploadImageResponse = imageStorageService.uploadImageToFileSystem(plantId, imageUpload);

        if (isSelected) {
            boolean existImages = imageRepository.existsByPlant(plantId);
            if (existImages) {
                int rowsAffected = update("isSelected = FALSE WHERE plant.id = ?1", plantId);
                LOG.infof("%s images affected given plantId %s", rowsAffected, plantId);
            }
        }

        var imageToPersist = new Image(
                uploadImageResponse.storagePath(),
                uploadImageResponse.filename(),
                imageUpload.size(),
                imageUpload.contentType(),
                isSelected
        );

        persistedPlant.addImage(imageToPersist);
        return new PlantImageResponse(imageToPersist);
    }

    public DownloadImageResponse obtainImageToPlantCardBy(Long plantId) {
        var imageCardResponse = imageRepository.fetchImagePlantCardOrThrowException(plantId);
        LOG.infof("Downloading image for plant card with id: %s", plantId);
        byte[] imageBytes = imageStorageService.downloadImageFromFileSystem(
                imageCardResponse.filename(), imageCardResponse.storagePath());
        return new DownloadImageResponse(imageBytes, imageCardResponse.contentType());
    }
}
