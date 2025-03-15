package com.pokemon.integration;

import com.pokemon.PokedexApplication;
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
        String standardDescription = "When several of these POKéMON gather, their electricity could build and cause lightning storms.";
        String shakespeareDescription = "At which hour several of these pokémon gather,  their electricity couldst buildeth and cause lightning storms.";

        webTestClient.get()
                .uri("/pokemon/translated/pikachu")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("pikachu")
                .jsonPath("$.habitat").isEqualTo("forest")
                .jsonPath("$.legendary").isEqualTo(false)
                .jsonPath("$.description").value(desc ->
                        Assertions.assertTrue(
                                desc.equals(shakespeareDescription) || desc.equals(standardDescription),
                                "The description does not match any expected values"
                        )
                );
    }

    @Test
    void getTranslatedMewtwo_returnsValidTranslation() {
        String standardDescription = "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.";
        String yodaDescription = "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was.";

        webTestClient.get()
                .uri("/pokemon/translated/mewtwo")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("mewtwo")
                .jsonPath("$.habitat").isEqualTo("rare")
                .jsonPath("$.legendary").isEqualTo(true)
                .jsonPath("$.description").value(desc ->
                        Assertions.assertTrue(
                                desc.equals(yodaDescription) || desc.equals(standardDescription),
                                "The description does not match any expected values"
                        )
                );
    }

    @Test
    void getTranslatedZubat_returnsValidTranslation() {
        String standardDescription = "Forms colonies in perpetually dark places. Uses ultrasonic waves to identify and approach targets.";
        String yodaDescription = "Forms colonies in perpetually dark places.Ultrasonic waves to identify and approach targets,  uses.";

        webTestClient.get()
                .uri("/pokemon/translated/zubat")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("zubat")
                .jsonPath("$.habitat").isEqualTo("cave")
                .jsonPath("$.legendary").isEqualTo(false)
                .jsonPath("$.description").value(desc ->
                        Assertions.assertTrue(
                                desc.equals(yodaDescription) || desc.equals(standardDescription),
                                "The description does not match any expected values"
                        )
                );
    }

    @Test
    void getTranslatedNonExistentPokemon_returns404() {
        webTestClient.get()
                .uri("/pokemon/translated/nonexistentpokemon12345")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isNotEmpty();
    }
}