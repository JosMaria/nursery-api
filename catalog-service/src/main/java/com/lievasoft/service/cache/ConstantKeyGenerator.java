package com.lievasoft.service.cache;

import io.quarkus.cache.CacheKeyGenerator;
import io.quarkus.cache.DefaultCacheKey;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Method;

@ApplicationScoped
public class ConstantKeyGenerator implements CacheKeyGenerator {

    @Override
    public Object generate(Method method, Object... methodParams) {
        return new DefaultCacheKey("plant-cards-list");
    }
}
