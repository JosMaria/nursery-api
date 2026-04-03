package com.lievasoft.service.cache;

import io.quarkus.cache.CacheKeyGenerator;
import io.quarkus.cache.DefaultCacheKey;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Method;

@ApplicationScoped
public class PlantCardKeyGenerator implements CacheKeyGenerator {

    public static final String PLANT_CARDS_LIST_CACHE = "plant-cards-list";

    @Override
    public Object generate(Method method, Object... methodParams) {
        return new DefaultCacheKey(PLANT_CARDS_LIST_CACHE);
    }
}
