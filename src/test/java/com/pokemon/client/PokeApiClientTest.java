package com.pokemon.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.dto.pokeapi.FlavorTextEntry;
import com.pokemon.dto.pokeapi.PokemonSpeciesDto;
import com.pokemon.exception.PokemonNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PokeApiClientTest {

    private static MockWebServer mockWebServer;
    private PokeApiClient pokeApiClient;

    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void shutdownServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        pokeApiClient = new PokeApiClient(webClient);
    }

    @Test
    void getPokemonSpecies_shouldReturnPokemonData() throws JsonProcessingException {
        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName("rare");

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName("en");

        FlavorTextEntry flavorTextEntry = new FlavorTextEntry();
        flavorTextEntry.setFlavorText("It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.");
        flavorTextEntry.setLanguage(english);

        PokemonSpeciesDto mewtwo = new PokemonSpeciesDto();
        mewtwo.setName("mewtwo");
        mewtwo.setFlavorTextEntries(List.of(flavorTextEntry));
        mewtwo.setHabitat(habitat);
        mewtwo.setLegendary(true);

        String mockResponseBody = new ObjectMapper().writeValueAsString(mewtwo);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponseBody)
        );

        var result = pokeApiClient.getPokemonSpecies("mewtwo");

        StepVerifier.create(result)
                .expectNextMatches(pokemon ->
                        pokemon.getName().equals("mewtwo") &&
                                pokemon.isLegendary() &&
                                pokemon.getHabitat().getName().equals("rare") &&
                                pokemon.getFlavorTextEntries().get(0).getFlavorText().contains("scientist after years"))
                .verifyComplete();
    }

    @Test
    void getPokemonSpecies_shouldThrowPokemonNotFoundException_whenNotFound() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"error\": \"Not found\"}")
        );

        var result = pokeApiClient.getPokemonSpecies("nonexistent");

        StepVerifier.create(result)
                .expectError(PokemonNotFoundException.class)
                .verify();
    }

    @Test
    void getPokemonSpecies_shouldPropagateOriginalError_whenServerError() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"error\": \"Internal Server Error\"}")
        );

        var result = pokeApiClient.getPokemonSpecies("pikachu");

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(WebClientResponseException.class);
                    WebClientResponseException wcError = (WebClientResponseException) error;
                    assertThat(wcError.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                })
                .verify();
    }

    @Test
    void getPokemonSpecies_shouldHandleOtherErrors() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"error\": \"Bad Request\"}")
        );

        StepVerifier.create(pokeApiClient.getPokemonSpecies("nonexistent"))
                .expectError(WebClientResponseException.class)
                .verify();
    }
}