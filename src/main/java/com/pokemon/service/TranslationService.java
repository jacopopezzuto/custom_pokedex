package com.pokemon.service;

import com.pokemon.client.TranslationApiClient;
import com.pokemon.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationApiClient translationApiClient;

    @Cacheable(value = AppConstants.Cache.TRANSLATION_CACHE,
            key = "'" + AppConstants.Api.SHAKESPEARE_TRANSLATION + ":' + #text",
            sync = false)
    public Mono<String> translateToShakespeare(String text) {
        log.debug("Translating to Shakespeare");
        return translationApiClient.translateToShakespeare(text);
    }

    @Cacheable(value = AppConstants.Cache.TRANSLATION_CACHE,
            key = "'" + AppConstants.Api.YODA_TRANSLATION + ":' + #text",
            sync = false)
    public Mono<String> translateToYoda(String text) {
        log.debug("Translating to Yoda");
        return translationApiClient.translateToYoda(text);
    }
}