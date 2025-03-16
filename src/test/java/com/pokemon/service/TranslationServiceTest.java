package com.pokemon.service;

import com.pokemon.client.TranslationApiClient;
import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock
    private TranslationApiClient translationApiClient;

    @InjectMocks
    private TranslationService translationService;

    @Test
    void translateToShakespeare_shouldReturnTranslatedText() {
        when(translationApiClient.translateToShakespeare(TestConstants.Descriptions.PIKACHU_STANDARD))
                .thenReturn(Mono.just(TestConstants.Descriptions.PIKACHU_SHAKESPEARE));

        Mono<String> result = translationService.translateToShakespeare(TestConstants.Descriptions.PIKACHU_STANDARD);

        StepVerifier.create(result)
                .expectNext(TestConstants.Descriptions.PIKACHU_SHAKESPEARE)
                .verifyComplete();
    }

    @Test
    void translateToYoda_shouldReturnTranslatedText() {
        when(translationApiClient.translateToYoda(TestConstants.Descriptions.ZUBAT_STANDARD))
                .thenReturn(Mono.just(TestConstants.Descriptions.ZUBAT_YODA));

        Mono<String> result = translationService.translateToYoda(TestConstants.Descriptions.ZUBAT_STANDARD);

        StepVerifier.create(result)
                .expectNext(TestConstants.Descriptions.ZUBAT_YODA)
                .verifyComplete();
    }
}