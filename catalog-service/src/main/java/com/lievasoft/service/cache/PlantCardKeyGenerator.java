package com.lievasoft.service.cache;

import io.quarkus.cache.CacheKeyGenerator;
import io.quarkus.cache.CompositeCacheKey;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Method;

@ApplicationScoped
public class PlantCardKeyGenerator implements CacheKeyGenerator {

    public static final String PLANT_CARDS_CACHE = "plant-cards";
    public static final int CACHED_NUMBER_PAGE = 0;
    public static final int CACHED_SIZE_PAGE = 8;

    @Override
    public Object generate(Method method, Object... methodParams) {
        return new CompositeCacheKey(CACHED_NUMBER_PAGE, CACHED_SIZE_PAGE);
    }
}
