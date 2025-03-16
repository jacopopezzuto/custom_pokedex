package com.pokemon.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.exception.PokemonNotFoundException;
import com.pokemon.util.TestConstants;
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
        String mockResponseBody = new ObjectMapper().writeValueAsString(TestConstants.TestObjects.MEWTWO_SPECIES_DTO);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponseBody)
        );

        var result = pokeApiClient.getPokemonSpecies(TestConstants.Names.MEWTWO);

        StepVerifier.create(result)
                .expectNextMatches(pokemon ->
                        pokemon.getName().equals(TestConstants.Names.MEWTWO) &&
                                pokemon.isLegendary() &&
                                pokemon.getHabitat().getName().equals(TestConstants.Habitats.RARE) &&
                                pokemon.getFlavorTextEntries().get(0).getFlavorText().equals(TestConstants.Descriptions.PIKACHU_STANDARD))
                .verifyComplete();
    }


    @Test
    void getPokemonSpecies_shouldThrowPokemonNotFoundException_whenNotFound() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(TestConstants.ErrorResponses.NOT_FOUND)
        );

        var result = pokeApiClient.getPokemonSpecies(TestConstants.Names.NONEXISTENT_POKEMON);

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
                        .setBody(TestConstants.ErrorResponses.INTERNAL_SERVER_ERROR)
        );

        var result = pokeApiClient.getPokemonSpecies(TestConstants.Names.PIKACHU);

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
                        .setBody(TestConstants.ErrorResponses.BAD_REQUEST)
        );

        StepVerifier.create(pokeApiClient.getPokemonSpecies(TestConstants.Names.NONEXISTENT_POKEMON))
                .expectError(WebClientResponseException.class)
                .verify();
    }
}