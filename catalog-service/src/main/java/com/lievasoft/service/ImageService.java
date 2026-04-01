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

    public ImageService(ImageStorageService imageStorageService, PlantRepository plantRepository, ImageRepository imageRepository) {
        this.imageStorageService = imageStorageService;
        this.plantRepository = plantRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public PlantImageResponse persist(Long plantId, FileUpload imageUpload) {
        var persistedPlant = plantRepository.obtainByIdOrThrowException(plantId);
        var uploadImageResponse = imageStorageService.uploadImageToFileSystem(plantId, imageUpload);
        var imageToPersist = new Image(
                uploadImageResponse.storagePath(),
                uploadImageResponse.filename(),
                imageUpload.size(),
                imageUpload.contentType()
        );

        persistedPlant.addImage(imageToPersist);
        return new PlantImageResponse(imageToPersist);
    }

    public DownloadImageResponse obtainImageToPlantCardBy(Long plantId) {
        var imageCardResponse = imageRepository.getImagePlantCardOrThrowException(plantId);
        LOG.infof("Downloading image for plant card with id: %s", plantId);
        byte[] imageBytes = imageStorageService.downloadImageFromFileSystem(
                imageCardResponse.filename(), imageCardResponse.storagePath());
        return new DownloadImageResponse(imageBytes, imageCardResponse.contentType());
    }
}
