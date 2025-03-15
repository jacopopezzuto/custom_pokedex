package com.pokemon.service;

import com.pokemon.client.TranslationApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationApiClient translationApiClient;

    public Mono<String> translateToShakespeare(String text) {
        log.debug("Translating to Shakespeare");
        return translationApiClient.translateToShakespeare(text);
    }

    public Mono<String> translateToYoda(String text) {
        log.debug("Translating to Yoda");
        return translationApiClient.translateToYoda(text);
    }
}