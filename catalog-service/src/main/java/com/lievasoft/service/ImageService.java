package com.lievasoft.service;

import com.lievasoft.dto.plant.PlantImageResponse;
import com.lievasoft.entity.Image;
import com.lievasoft.repository.PlantRepository;
import com.lievasoft.service.storage.ImageStorageService;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class ImageService {

    private final ImageStorageService imageStorageService;
    private final PlantRepository plantRepository;

    public ImageService(ImageStorageService imageStorageService, PlantRepository plantRepository) {
        this.imageStorageService = imageStorageService;
        this.plantRepository = plantRepository;
    }

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
}
