package com.pokemon.config;

import com.pokemon.constants.AppConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CacheConfigTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    void cacheManagerShouldContainExpectedCaches() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        Set<String> cacheNameSet = new HashSet<>(cacheNames);

        assertThat(cacheNameSet)
                .contains(AppConstants.Cache.POKEMON_CACHE, AppConstants.Cache.TRANSLATION_CACHE);
    }

    @Test
    void cacheShouldBeOfTypeCaffeine() {
        assertThat(cacheManager)
                .satisfies(manager -> {
                    assertThat(manager.getCache(AppConstants.Cache.POKEMON_CACHE))
                            .isInstanceOf(CaffeineCache.class);
                    assertThat(manager.getCache(AppConstants.Cache.TRANSLATION_CACHE))
                            .isInstanceOf(CaffeineCache.class);
                });
    }
}