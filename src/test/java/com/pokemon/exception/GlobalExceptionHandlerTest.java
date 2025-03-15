package com.pokemon.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlePokemonNotFoundException_shouldReturnNotFoundStatus() {
        PokemonNotFoundException exception = new PokemonNotFoundException("pikachu");

        ResponseEntity<Map<String, String>> response = handler.handlePokemonNotFoundException(exception);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(r.getBody())
                            .containsEntry("error", "Pokemon not found with name: pikachu");
                });
    }

    @Test
    void handleTranslationException_shouldReturnOkStatus() {
        TranslationException exception = new TranslationException("Translation service unavailable");

        ResponseEntity<Map<String, String>> response = handler.handleTranslationException(exception);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(r.getBody())
                            .containsEntry("error", "Could not apply translation. Using standard description instead.");
                });
    }

    @Test
    void handleWebClientResponseException_shouldReturnInternalServerError() {
        WebClientResponseException exception = mock(WebClientResponseException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(exception.getMessage()).thenReturn("Bad Request");

        ResponseEntity<Map<String, String>> response = handler.handleWebClientResponseException(exception);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                    assertThat(r.getBody())
                            .containsEntry("error", "External API error: 400 BAD_REQUEST");
                });
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<Map<String, String>> response = handler.handleGenericException(exception);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                    assertThat(r.getBody())
                            .containsEntry("error", "An unexpected error occurred");
                });
    }
}