package com.lievasoft.service;

import com.lievasoft.dto.plant.PaginatedResult;
import com.lievasoft.dto.plant.PlantCreateDTO;
import com.lievasoft.dto.plant.PlantCreateResponse;
import com.lievasoft.dto.response.PlantCardResponse;
import com.lievasoft.dto.response.PlantDetailsResponse;
import com.lievasoft.entity.CommonName;
import com.lievasoft.entity.Plant;
import com.lievasoft.entity.Taxonomy;
import com.lievasoft.repository.CommonNameRepository;
import com.lievasoft.repository.ImageRepository;
import com.lievasoft.repository.PlantRepository;
import com.lievasoft.service.cache.PlantCardKeyGenerator;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.stream.Collectors;

import static com.lievasoft.service.cache.PlantCardKeyGenerator.*;

@ApplicationScoped
public class PlantService {

    private static final Logger LOG = Logger.getLogger(PlantService.class);

    private final HashCommands<String, String, String> hashCommands;
    private final KeyCommands<String> keyCommands;
    private final PlantRepository plantRepository;
    private final CommonNameRepository commonNameRepository;
    private final ImageRepository imageRepository;

    public PlantService(RedisDataSource redisDataSource,
                        PlantRepository plantRepository,
                        CommonNameRepository commonNameRepository, ImageRepository imageRepository) {
        this.hashCommands = redisDataSource.hash(String.class);
        this.keyCommands = redisDataSource.key();
        this.plantRepository = plantRepository;
        this.commonNameRepository = commonNameRepository;
        this.imageRepository = imageRepository;
    }

    @CacheInvalidate(cacheName = PLANT_CARDS_CACHE, keyGenerator = PlantCardKeyGenerator.class)
    public PlantCreateResponse create(PlantCreateDTO plantCreateDTO) {
        var plantToPersist = new Plant(plantCreateDTO);
        var taxonomyToPersist = new Taxonomy(plantCreateDTO.taxonomyDTO());
        var commonNamesToPersist = plantCreateDTO.commonNamesDTO()
                .stream()
                .map(CommonName::new)
                .collect(Collectors.toSet());

        plantToPersist.setTaxonomy(taxonomyToPersist);
        plantToPersist.addCommonNames(commonNamesToPersist);
        plantRepository.create(plantToPersist);

        return new PlantCreateResponse(
                plantToPersist,
                plantCreateDTO.taxonomyDTO(),
                plantCreateDTO.commonNamesDTO()
        );
    }

    public PaginatedResult<PlantCardResponse> obtainPlantCards(int numberPage, int sizePage) {
        if (numberPage == CACHED_NUMBER_PAGE && sizePage == CACHED_SIZE_PAGE)
            return obtainCachedPlantCards(numberPage, sizePage);
        else {
            LOG.infof("Obtaining plant cards from database (page=%s, size=%s)", numberPage, sizePage);
            return plantRepository.fetchPaginatedPlantCards(numberPage, sizePage);
        }
    }

    @CacheResult(cacheName = PLANT_CARDS_CACHE, keyGenerator = PlantCardKeyGenerator.class)
    PaginatedResult<PlantCardResponse> obtainCachedPlantCards(int numberPage, int sizePage) {
        LOG.infof("Caching default plant cards (page=0, size=8)");
        return plantRepository.fetchPaginatedPlantCards(numberPage, sizePage);
    }

    public PlantDetailsResponse obtainPlantDetailsById(Long plantId) {
        var plantTaxonomy = plantRepository.fetchPlantTaxonomyById(plantId);
        var commonNamesToPlantDetails = commonNameRepository.fetchCommonNameToPlantDetails(plantId);
        var imagesToPlantDetailsDTO = imageRepository.fetchImageUrlsByPlantId(plantId);
        return new PlantDetailsResponse(plantTaxonomy, imagesToPlantDetailsDTO, commonNamesToPlantDetails);
//        var key = "plant:details:%s".formatted(plantId);
//        Map<String, String> redisPlantHash = hashCommands.hgetall(key);
//
//        if (Objects.isNull(redisPlantHash) || redisPlantHash.isEmpty()) {
//
//            saveToRedisCache(key, plantDetailsResponse);
//            return null;
//
//        } else return new PlantDetailsResponse(redisPlantHash);
    }

//    private void saveToRedisCache(String key, PlantDetailsResponse plantDetails) {
//        Map<String, String> hashToPersist = plantDetails.mapToRedisHash();
//        this.hashCommands.hset(key, hashToPersist);
//        this.keyCommands.expire(key, Duration.ofMinutes(1));
//    }

    public boolean changeIsFavorite(long plantId, boolean isFavorite) {
        if (canChangeIsFavorite(isFavorite)) {
            int countModified = plantRepository.updateIsFavorite(plantId, isFavorite);
            return countModified == 1;

        } else return false;
    }

    private boolean canChangeIsFavorite(boolean isFavorite) {
        final int numberMaxOfFavorites = 4;
        if (isFavorite) {
            int countFavorites = plantRepository.countOfFavorites();
            return countFavorites < numberMaxOfFavorites;

        } else return true;
    }
}
