package com.pokemon.client;

import com.pokemon.constants.AppConstants;
import com.pokemon.dto.translator.TranslationResponse;
import com.pokemon.exception.TranslationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranslationApiClient {

    private final WebClient webClient;

    @Value("${api.fun-api.base-url}")
    private String baseUrl;

    @Value("${api.fun-api.shakespeare-path}")
    private String shakespearePath;

    @Value("${api.fun-api.yoda-path}")
    private String yodaPath;

    /**
     * Translates text to Shakespeare style.
     *
     * @param text the text to translate
     * @return the translated text
     * @throws TranslationException if translation fails
     */
    public Mono<String> translateToShakespeare(String text) {
        return translate(text, shakespearePath, AppConstants.Api.SHAKESPEARE_TRANSLATION);
    }

    /**
     * Translates text to Yoda style.
     *
     * @param text the text to translate
     * @return the translated text
     * @throws TranslationException if translation fails
     */
    public Mono<String> translateToYoda(String text) {
        return translate(text, yodaPath, AppConstants.Api.YODA_TRANSLATION);
    }

    /**
     * Generic method to translate text using specified path and style.
     */
    private Mono<String> translate(String text, String path, String style) {
        String url = baseUrl + path;

        log.debug("Starting {} translation", style);

        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("text", text))
                .retrieve()
                .bodyToMono(TranslationResponse.class)
                .map(response -> response.getContents().getTranslated())
                .doOnSuccess(result -> log.debug("Successfully completed {} translation", style))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Error in {} translation: {}", style, ex.getMessage());
                    return Mono.error(new TranslationException("Translation service unavailable", ex));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("Unexpected error during {} translation", style, ex);
                    return Mono.error(new TranslationException("Failed to translate text", ex));
                });
    }
}