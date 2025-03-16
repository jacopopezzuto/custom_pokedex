package com.pokemon.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.exception.TranslationException;
import com.pokemon.util.TestConstants;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

class TranslationApiClientTest {

    private static MockWebServer mockWebServer;
    private TranslationApiClient translationApiClient;
    private ObjectMapper objectMapper = new ObjectMapper();

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

        translationApiClient = new TranslationApiClient(webClient);
    }

    @Test
    void translateToShakespeare_shouldReturnTranslatedText() throws JsonProcessingException {
        String mockResponseBody = new ObjectMapper().writeValueAsString(TestConstants.TestObjects.SHAKESPEARE_TRANSLATION_RESPONSE);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponseBody)
        );

        var result = translationApiClient.translateToShakespeare(TestConstants.Descriptions.PIKACHU_STANDARD);

        StepVerifier.create(result)
                .expectNext(TestConstants.Descriptions.PIKACHU_SHAKESPEARE)
                .verifyComplete();
    }


    @Test
    void translateToYoda_shouldReturnTranslatedText() throws JsonProcessingException {
        String mockResponseBody = objectMapper.writeValueAsString(TestConstants.TestObjects.YODA_TRANSLATION_RESPONSE);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponseBody)
        );

        var result = translationApiClient.translateToYoda(TestConstants.Descriptions.ZUBAT_STANDARD);

        StepVerifier.create(result)
                .expectNext(TestConstants.Descriptions.ZUBAT_YODA)
                .verifyComplete();
    }

    @Test
    void translate_shouldThrowTranslationException_whenApiResponseCodeIsNot200() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(429)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(TestConstants.ErrorResponses.TOO_MANY_REQUESTS)
        );

        var result = translationApiClient.translateToShakespeare(TestConstants.Descriptions.PIKACHU_STANDARD);

        StepVerifier.create(result)
                .expectError(TranslationException.class)
                .verify();
    }
}