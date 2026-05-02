package com.lievasoft.service;

import com.lievasoft.dto.request.plant.PlantCreateDTO;
import com.lievasoft.dto.response.plant.*;
import com.lievasoft.entity.CommonName;
import com.lievasoft.entity.Plant;
import com.lievasoft.entity.Taxonomy;
import com.lievasoft.repository.CommonNameRepository;
import com.lievasoft.repository.ImageRepository;
import com.lievasoft.repository.PlantRepository;
import com.lievasoft.service.cache.PlantCardPageKeyGenerator;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.lievasoft.service.cache.PlantCardPageKeyGenerator.*;

@ApplicationScoped
public class DefaultPlantService implements PlantService {

    private static final Logger LOG = Logger.getLogger(DefaultPlantService.class);

    private final HashCommands<String, String, String> hashCommands;
    private final KeyCommands<String> keyCommands;
    private final PlantRepository plantRepository;
    private final CommonNameRepository commonNameRepository;
    private final ImageRepository imageRepository;

    public DefaultPlantService(RedisDataSource redisDataSource,
                               PlantRepository plantRepository,
                               CommonNameRepository commonNameRepository, ImageRepository imageRepository) {
        this.hashCommands = redisDataSource.hash(String.class);
        this.keyCommands = redisDataSource.key();
        this.plantRepository = plantRepository;
        this.commonNameRepository = commonNameRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    @CacheInvalidate(cacheName = PLANT_CARD_PAGE_CACHE, keyGenerator = PlantCardPageKeyGenerator.class)
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

    @Override
    public List<PlantSummaryResponse> obtainPlantSummaries() {
        return plantRepository.obtainPlantsSummary();
    }

    @Override
    public PaginatedResult<PlantCardResponse> obtainPlantCardPage(int numberPage, int sizePage) {
        if (numberPage == CACHED_NUMBER_PAGE && sizePage == CACHED_SIZE_PAGE)
            return obtainCachedPlantCardPage();
        else {
            LOG.infof("Obtaining plant card page from database (page=%s, size=%s)", numberPage, sizePage);
            return plantRepository.fetchPaginatedPlantCards(numberPage, sizePage);
        }
    }

    @CacheResult(cacheName = PLANT_CARD_PAGE_CACHE, keyGenerator = PlantCardPageKeyGenerator.class)
    PaginatedResult<PlantCardResponse> obtainCachedPlantCardPage() {
        String logMessage = "Obtaining plant card page from cache (page=%s, size=%s)";
        LOG.infof(logMessage, CACHED_NUMBER_PAGE, CACHED_SIZE_PAGE);
        return plantRepository.fetchPaginatedPlantCards(CACHED_NUMBER_PAGE, CACHED_SIZE_PAGE);
    }

    @Override
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

    @Override
    public boolean changeFavoritePlant(long plantId, boolean isFavorite) {
        if (isFavorite) {
            final int numberMaxOfFavorites = 4;
            boolean canChange = plantRepository.count("isFavorite = TRUE") < numberMaxOfFavorites;
            if (!canChange) return false;
        }

        int countModified = plantRepository.updateIsFavorite(plantId, isFavorite);
        return countModified == 1;
    }
}
