package com.pokemon.integration;

import com.pokemon.PokedexApplication;
import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = PokedexApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Tag("realApi")
class BasicPokemonApiIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getPikachu_returnsCorrectDetails() {
        webTestClient.get()
                .uri("/pokemon/" + TestConstants.Names.PIKACHU)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TestConstants.Names.PIKACHU)
                .jsonPath("$.description").isEqualTo(TestConstants.Descriptions.PIKACHU_STANDARD)
                .jsonPath("$.habitat").isEqualTo(TestConstants.Habitats.FOREST)
                .jsonPath("$.legendary").isEqualTo(TestConstants.Legendary.FALSE);
    }

    @Test
    void getMewtwo_returnsLegendaryPokemon() {
        webTestClient.get()
                .uri("/pokemon/" + TestConstants.Names.MEWTWO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TestConstants.Names.MEWTWO)
                .jsonPath("$.habitat").isEqualTo(TestConstants.Habitats.RARE)
                .jsonPath("$.legendary").isEqualTo(TestConstants.Legendary.TRUE)
                .jsonPath("$.description").isEqualTo(TestConstants.Descriptions.MEWTWO_STANDARD);
    }

    @Test
    void getZubat_returnsCaveHabitat() {
        webTestClient.get()
                .uri("/pokemon/" + TestConstants.Names.ZUBAT)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TestConstants.Names.ZUBAT)
                .jsonPath("$.description").isEqualTo(TestConstants.Descriptions.ZUBAT_STANDARD)
                .jsonPath("$.habitat").isEqualTo(TestConstants.Habitats.CAVE)
                .jsonPath("$.legendary").isEqualTo(TestConstants.Legendary.FALSE);
    }

    @Test
    void getNonExistentPokemon_returns404() {
        webTestClient.get()
                .uri("/pokemon/" + TestConstants.Names.NONEXISTENT_POKEMON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isNotEmpty();
    }
}