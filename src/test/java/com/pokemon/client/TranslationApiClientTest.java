package com.pokemon.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.dto.translator.TranslationResponse;
import com.pokemon.exception.TranslationException;
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
        TranslationResponse response = new TranslationResponse();

        TranslationResponse.Success success = new TranslationResponse.Success();
        success.setTotal(1);

        TranslationResponse.Contents contents = new TranslationResponse.Contents();
        contents.setTranslated("At which hour several of these pokémon gather, their electricity couldst buildeth and cause lightning storms.");
        contents.setText("When several of these POKéMON gather, their electricity could build and cause lightning storms.");
        contents.setTranslation("shakespeare");

        response.setSuccess(success);
        response.setContents(contents);

        String mockResponseBody = objectMapper.writeValueAsString(response);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponseBody)
        );

        var result = translationApiClient.translateToShakespeare("When several of these POKéMON gather, their electricity could build and cause lightning storms.");

        StepVerifier.create(result)
                .expectNext("At which hour several of these pokémon gather, their electricity couldst buildeth and cause lightning storms.")
                .verifyComplete();
    }

    @Test
    void translateToYoda_shouldReturnTranslatedText() throws JsonProcessingException {
        TranslationResponse response = new TranslationResponse();

        TranslationResponse.Success success = new TranslationResponse.Success();
        success.setTotal(1);

        TranslationResponse.Contents contents = new TranslationResponse.Contents();
        contents.setTranslated("Forms colonies in perpetually dark places. Ultrasonic waves to identify and approach targets, uses.");
        contents.setText("Forms colonies in perpetually dark places. Uses ultrasonic waves to identify and approach targets.");
        contents.setTranslation("yoda");

        response.setSuccess(success);
        response.setContents(contents);

        String mockResponseBody = objectMapper.writeValueAsString(response);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(mockResponseBody)
        );

        var result = translationApiClient.translateToYoda("Forms colonies in perpetually dark places. Uses ultrasonic waves to identify and approach targets.");

        StepVerifier.create(result)
                .expectNext("Forms colonies in perpetually dark places. Ultrasonic waves to identify and approach targets, uses.")
                .verifyComplete();
    }

    @Test
    void translate_shouldThrowTranslationException_whenApiResponseCodeIsNot200() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(429)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"error\": {\"code\": 429, \"message\": \"Too many requests\"}}")
        );

        var result = translationApiClient.translateToShakespeare("text to translate");

        StepVerifier.create(result)
                .expectError(TranslationException.class)
                .verify();
    }
}