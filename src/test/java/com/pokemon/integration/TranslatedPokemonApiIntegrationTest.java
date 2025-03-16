package com.pokemon.integration;

import com.pokemon.PokedexApplication;
import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = PokedexApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Tag("realApi")
class TranslatedPokemonApiIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getTranslatedPikachu_returnsValidTranslation() {
        webTestClient.get()
                .uri("/pokemon/translated/" + TestConstants.Names.PIKACHU)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TestConstants.Names.PIKACHU)
                .jsonPath("$.habitat").isEqualTo(TestConstants.Habitats.FOREST)
                .jsonPath("$.legendary").isEqualTo(TestConstants.Legendary.FALSE)
                .jsonPath("$.description").value(desc ->
                        Assertions.assertTrue(
                                desc.equals(TestConstants.Descriptions.PIKACHU_SHAKESPEARE) ||
                                        desc.equals(TestConstants.Descriptions.PIKACHU_STANDARD),
                                TestConstants.ErrorMessages.DESCRIPTION_MISMATCH_ERROR
                        )
                );
    }

    @Test
    void getTranslatedMewtwo_returnsValidTranslation() {
        webTestClient.get()
                .uri("/pokemon/translated/" + TestConstants.Names.MEWTWO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TestConstants.Names.MEWTWO)
                .jsonPath("$.habitat").isEqualTo(TestConstants.Habitats.RARE)
                .jsonPath("$.legendary").isEqualTo(TestConstants.Legendary.TRUE)
                .jsonPath("$.description").value(desc ->
                        Assertions.assertTrue(
                                desc.equals(TestConstants.Descriptions.MEWTWO_YODA) ||
                                        desc.equals(TestConstants.Descriptions.MEWTWO_STANDARD),
                                TestConstants.ErrorMessages.DESCRIPTION_MISMATCH_ERROR
                        )
                );
    }

    @Test
    void getTranslatedZubat_returnsValidTranslation() {
        webTestClient.get()
                .uri("/pokemon/translated/" + TestConstants.Names.ZUBAT)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TestConstants.Names.ZUBAT)
                .jsonPath("$.habitat").isEqualTo(TestConstants.Habitats.CAVE)
                .jsonPath("$.legendary").isEqualTo(TestConstants.Legendary.FALSE)
                .jsonPath("$.description").value(desc ->
                        Assertions.assertTrue(
                                desc.equals(TestConstants.Descriptions.ZUBAT_YODA) ||
                                        desc.equals(TestConstants.Descriptions.ZUBAT_STANDARD),
                                TestConstants.ErrorMessages.DESCRIPTION_MISMATCH_ERROR
                        )
                );
    }

    @Test
    void getTranslatedNonExistentPokemon_returns404() {
        webTestClient.get()
                .uri("/pokemon/translated/" + TestConstants.Names.NONEXISTENT_POKEMON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isNotEmpty();
    }
}