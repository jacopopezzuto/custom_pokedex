package com.pokemon.integration;

import com.pokemon.PokedexApplication;
import com.pokemon.client.PokeApiClient;
import com.pokemon.client.TranslationApiClient;
import com.pokemon.controller.PokemonController;
import com.pokemon.service.PokemonService;
import com.pokemon.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PokedexApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PokemonIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PokemonController pokemonController;

    @Autowired
    private PokemonService pokemonService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private PokeApiClient pokeApiClient;

    @Autowired
    private TranslationApiClient translationApiClient;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void applicationContextLoads() {
        assertThat(pokemonController).isNotNull();
        assertThat(pokemonService).isNotNull();
        assertThat(translationService).isNotNull();
        assertThat(pokeApiClient).isNotNull();
        assertThat(translationApiClient).isNotNull();
        assertThat(cacheManager).isNotNull();

        assertThat(port).isPositive();

        assertThat(cacheManager.getCacheNames()).isNotEmpty();
    }

    @Test
    void endpointsAreAccessible() {

        webTestClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }
}