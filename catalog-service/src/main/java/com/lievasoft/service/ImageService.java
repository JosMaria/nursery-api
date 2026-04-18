package com.lievasoft.service;

import com.lievasoft.dto.plant.PlantImageResponse;
import com.lievasoft.dto.response.DownloadImageResponse;
import com.lievasoft.entity.Image;
import com.lievasoft.repository.ImageRepository;
import com.lievasoft.repository.PlantRepository;
import com.lievasoft.service.storage.ImageStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
public class ImageService {

    private static final Logger LOG = Logger.getLogger(ImageService.class);

    private final ImageStorageService imageStorageService;
    private final PlantRepository plantRepository;
    private final ImageRepository imageRepository;

    public ImageService(ImageStorageService imageStorageService,
                        PlantRepository plantRepository,
                        ImageRepository imageRepository) {
        this.imageStorageService = imageStorageService;
        this.plantRepository = plantRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public PlantImageResponse persist(long plantId, boolean isSelected, FileUpload imageUpload) {
        var persistedPlant = plantRepository.obtainByIdOrThrowException(plantId);
        var uploadImageResponse = imageStorageService.uploadImageToFileSystem(plantId, imageUpload);

        if (isSelected) {
            boolean existImages = imageRepository.existsByPlant(plantId);
            if (existImages) {
                int rowsAffected = imageRepository.updateIsSelectedToFalseByPlant(plantId);
                LOG.infof("%s images affected given plant id %s", rowsAffected, plantId);
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

    public DownloadImageResponse obtainImagePlantBy(Long plantId, String filename) {
        var obtainedImage = imageRepository.findImagePlantBy(plantId, filename);
        LOG.infof("Downloading image for plant with id: %s", plantId);
        byte[] imageBytes = imageStorageService.downloadImageFromFileSystem(filename, obtainedImage.getStoragePath());
        return new DownloadImageResponse(imageBytes, obtainedImage.getContentType());
    }
}
