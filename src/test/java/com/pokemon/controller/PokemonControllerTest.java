package com.pokemon.controller;

import com.pokemon.dto.PokemonResponse;
import com.pokemon.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(PokemonController.class)
class PokemonControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PokemonService pokemonService;

    @Test
    void getPokemon_shouldReturnPokemonInfo() {
        PokemonResponse mewtwo = PokemonResponse.builder()
                .name("mewtwo")
                .description("It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.")
                .habitat("rare")
                .isLegendary(true)
                .build();

        when(pokemonService.getPokemonInfo("mewtwo")).thenReturn(Mono.just(mewtwo));

        webTestClient.get()
                .uri("/pokemon/mewtwo")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PokemonResponse.class)
                .isEqualTo(mewtwo);
    }

    @Test
    void getTranslatedPokemon_shouldReturnTranslatedPokemonInfo() {
        PokemonResponse translatedMewtwo = PokemonResponse.builder()
                .name("mewtwo")
                .description("Created by a scientist after years of horrific gene splicing and dna engineering experiments, it was.")
                .habitat("rare")
                .isLegendary(true)
                .build();

        when(pokemonService.getTranslatedPokemonInfo("mewtwo")).thenReturn(Mono.just(translatedMewtwo));

        webTestClient.get()
                .uri("/pokemon/translated/mewtwo")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PokemonResponse.class)
                .isEqualTo(translatedMewtwo);
    }
}