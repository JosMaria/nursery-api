package com.lievasoft.service;

import com.lievasoft.dto.plant.PlantCreateDTO;
import com.lievasoft.dto.plant.PlantCreateResponse;
import com.lievasoft.dto.plant.PlantImageResponse;
import com.lievasoft.dto.plant.PlantResponse;
import com.lievasoft.dto.response.PlantCardResponse;
import com.lievasoft.dto.response.PlantDetailsResponse;
import com.lievasoft.entity.CommonName;
import com.lievasoft.entity.Image;
import com.lievasoft.entity.Plant;
import com.lievasoft.entity.Taxonomy;
import com.lievasoft.enums.ImageExtension;
import com.lievasoft.exception.PlantNotFoundException;
import com.lievasoft.repository.PlantRepository;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.runtime.cdi.QuarkusArcBeanContainer;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class PlantService {

    private static final Logger LOG = Logger.getLogger(PlantService.class);
    private static final String IMAGE_PATH = "/Users/josmaria/project/images";

    private final HashCommands<String, String, String> hashCommands;
    private final KeyCommands<String> keyCommands;
    private final PlantRepository plantRepository;
    private final QuarkusArcBeanContainer quarkusArcBeanContainer;

    public PlantService(RedisDataSource redisDataSource, PlantRepository plantRepository, QuarkusArcBeanContainer quarkusArcBeanContainer) {
        this.hashCommands = redisDataSource.hash(String.class);
        this.keyCommands = redisDataSource.key();
        this.plantRepository = plantRepository;
        this.quarkusArcBeanContainer = quarkusArcBeanContainer;
    }

    public PlantCreateResponse create(PlantCreateDTO plantCreateDTO) {
        var plantToPersist = new Plant(plantCreateDTO);
        var taxonomyToPersist = new Taxonomy(plantCreateDTO.taxonomyDTO());
        Set<CommonName> commonNamesToPersist = plantCreateDTO.commonNamesDTO()
                .stream()
                .map(CommonName::new)
                .collect(Collectors.toSet());

        plantToPersist.addTaxonomy(taxonomyToPersist);
        plantToPersist.addCommonNames(commonNamesToPersist);
        plantRepository.create(plantToPersist);

        return new PlantCreateResponse(
                plantToPersist,
                plantCreateDTO.taxonomyDTO(),
                plantCreateDTO.commonNamesDTO()
        );
    }

    @Transactional
    public PlantImageResponse insertImage(Long plantId, FileUpload imageUpload) {
        var obtainedPlant = plantRepository.findByIdOptional(plantId)
                .orElseThrow(() -> new PlantNotFoundException(plantId));

//        if (imageUpload == null || imageUpload.filePath() == null)
//            throw new IllegalArgumentException("Image file is required");
        var prefixFileSystem = "file://";
        var uploadImageResponse = uploadToFileSystem(plantId, imageUpload);
        var urlLocal = prefixFileSystem + uploadImageResponse.path();
        var fileSize = imageUpload.size();
        var contentType = imageUpload.contentType();
        var imageToPersist = new Image(urlLocal, uploadImageResponse.filename, fileSize, contentType);
        obtainedPlant.addImage(imageToPersist);
        return new PlantImageResponse(imageToPersist);
    }

    private UploadImageResponse uploadToFileSystem(Long plantId, FileUpload imageUpload) {
        Path filePath = imageUpload.uploadedFile();
        if (filePath != null && Files.exists(filePath)) {
            try {
                Path directoryPath = Paths.get(IMAGE_PATH, "plant_%s".formatted(plantId));
                if (!Files.exists(directoryPath))
                    Files.createDirectories(directoryPath);

                byte[] imageBytes = Files.readAllBytes(filePath);
                var imageExtension = getExtensionByContentType(imageUpload.contentType());
                var filename = UUID.randomUUID() + "." + imageExtension.getExtension();
                var filePathToPersist = directoryPath.resolve(filename);
                Files.write(filePathToPersist, imageBytes);
                LOG.infof("Image saved in filesystem at directory: %s, filename: %s", directoryPath, filename);
                return new UploadImageResponse(filePathToPersist.toString(), filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else throw new IllegalArgumentException("Image file path is invalid");
    }

    record UploadImageResponse(
            String path,
            String filename
    ) {
    }

    private ImageExtension getExtensionByContentType(String contentType) {
        return switch (contentType) {
            case "image/webp" -> ImageExtension.WEBP;
            case "image/gif" -> ImageExtension.GIF;
            case "image/png" -> ImageExtension.PNG;
            case "image/jpeg" -> ImageExtension.JPEG;
            case "image/jpg" -> ImageExtension.JPG;
            default -> throw new IllegalArgumentException("Invalid content type for image");
        };
    }

    public PlantCreateResponse createCommonNames() {
        return null;
    }

    public PlantResponse removeById(Long plantId) {
        var obtainedPlant = plantRepository.findByIdOptional(plantId)
                .orElseThrow(() -> new PlantNotFoundException(plantId));
        plantRepository.remove(obtainedPlant);
        return new PlantResponse(obtainedPlant);
    }

    @CacheResult(cacheName = "plant-cards-list")
    public List<PlantCardResponse> fetchPlantCards() {
        return plantRepository.fetchPlantCards();
    }

    public PlantDetailsResponse fetchPlantDetailsById(Long plantId) {
        var key = "plant:details:%s".formatted(plantId);
        Map<String, String> redisPlantHash = hashCommands.hgetall(key);

        if (!(Objects.isNull(redisPlantHash) || redisPlantHash.isEmpty()))
            return new PlantDetailsResponse(redisPlantHash);

        else {
            var plantDetailsResponse = plantRepository.fetchPlantDetailsById(plantId)
                    .orElseThrow(() -> new PlantNotFoundException(plantId));
            saveToRedisCache(key, plantDetailsResponse);
            return plantDetailsResponse;
        }
    }

    private void saveToRedisCache(String key, PlantDetailsResponse plantDetails) {
        Map<String, String> hashToPersist = plantDetails.mapToRedisHash();
        this.hashCommands.hset(key, hashToPersist);
        this.keyCommands.expire(key, Duration.ofMinutes(1));
    }
}
