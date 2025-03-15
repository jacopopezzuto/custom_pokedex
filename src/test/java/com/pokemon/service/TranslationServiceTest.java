package com.pokemon.service;

import com.pokemon.client.TranslationApiClient;
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
        String original = "When several of these POKéMON gather, their electricity could build and cause lightning storms.";
        String translated = "At which hour several of these pokémon gather,  their electricity couldst buildeth and cause lightning storms.";

        when(translationApiClient.translateToShakespeare(original)).thenReturn(Mono.just(translated));

        Mono<String> result = translationService.translateToShakespeare(original);

        StepVerifier.create(result)
                .expectNext(translated)
                .verifyComplete();
    }

    @Test
    void translateToYoda_shouldReturnTranslatedText() {
        String original = "Forms colonies in perpetually dark places. Uses ultrasonic waves to identify and approach targets.";
        String translated = "Forms colonies in perpetually dark places.Ultrasonic waves to identify and approach targets,  uses.";


        when(translationApiClient.translateToYoda(original)).thenReturn(Mono.just(translated));

        Mono<String> result = translationService.translateToYoda(original);

        StepVerifier.create(result)
                .expectNext(translated)
                .verifyComplete();
    }
}