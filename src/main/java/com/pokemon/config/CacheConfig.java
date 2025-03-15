package com.pokemon.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.pokemon.constants.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.ttl.hours}")
    private int cacheTtlHours;

    @Value("${cache.max-size}")
    private int cacheMaxSize;

    /**
     * Configures the cache manager with Caffeine cache provider.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(Arrays.asList(
                AppConstants.Cache.POKEMON_CACHE,
                AppConstants.Cache.TRANSLATION_CACHE
        ));
        cacheManager.setAsyncCacheMode(true);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(cacheTtlHours, TimeUnit.HOURS)
                .maximumSize(cacheMaxSize)
                .recordStats());
        return cacheManager;
    }
}