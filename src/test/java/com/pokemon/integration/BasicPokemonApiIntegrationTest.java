package com.pokemon.integration;

import com.pokemon.PokedexApplication;
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
                .uri("/pokemon/pikachu")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("pikachu")
                .jsonPath("$.description").isEqualTo("When several of these POKéMON gather, their electricity could build and cause lightning storms.")
                .jsonPath("$.habitat").isEqualTo("forest")
                .jsonPath("$.legendary").isEqualTo(false);
    }

    @Test
    void getMewtwo_returnsLegendaryPokemon() {
        webTestClient.get()
                .uri("/pokemon/mewtwo")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("mewtwo")
                .jsonPath("$.habitat").isEqualTo("rare")
                .jsonPath("$.legendary").isEqualTo(true)
                .jsonPath("$.description").isEqualTo("It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.");
    }

    @Test
    void getZubat_returnsCaveHabitat() {
        webTestClient.get()
                .uri("/pokemon/zubat")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("zubat")
                .jsonPath("$.description").isEqualTo("Forms colonies in perpetually dark places. Uses ultrasonic waves to identify and approach targets.")
                .jsonPath("$.habitat").isEqualTo("cave")
                .jsonPath("$.legendary").isEqualTo(false);
    }

    @Test
    void getNonExistentPokemon_returns404() {
        webTestClient.get()
                .uri("/pokemon/nonexistent")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isNotEmpty();
    }
}