package com.pokemon.controller;

import com.pokemon.dto.PokemonResponse;
import com.pokemon.service.PokemonService;
import com.pokemon.util.TestConstants;
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
                .name(TestConstants.Names.MEWTWO)
                .description(TestConstants.Descriptions.MEWTWO_STANDARD)
                .habitat(TestConstants.Habitats.RARE)
                .isLegendary(TestConstants.Legendary.TRUE)
                .build();

        when(pokemonService.getPokemonInfo(TestConstants.Names.MEWTWO)).thenReturn(Mono.just(mewtwo));

        webTestClient.get()
                .uri("/pokemon/" + TestConstants.Names.MEWTWO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PokemonResponse.class)
                .isEqualTo(mewtwo);
    }

    @Test
    void getTranslatedPokemon_shouldReturnTranslatedPokemonInfo() {
        PokemonResponse translatedMewtwo = PokemonResponse.builder()
                .name(TestConstants.Names.MEWTWO)
                .description(TestConstants.Descriptions.MEWTWO_YODA)
                .habitat(TestConstants.Habitats.RARE)
                .isLegendary(TestConstants.Legendary.TRUE)
                .build();

        when(pokemonService.getTranslatedPokemonInfo(TestConstants.Names.MEWTWO)).thenReturn(Mono.just(translatedMewtwo));

        webTestClient.get()
                .uri("/pokemon/translated/" + TestConstants.Names.MEWTWO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PokemonResponse.class)
                .isEqualTo(translatedMewtwo);
    }
}