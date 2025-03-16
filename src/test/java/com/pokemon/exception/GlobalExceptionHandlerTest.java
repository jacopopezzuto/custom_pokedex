package com.pokemon.exception;

import com.pokemon.util.TestConstants;
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
        PokemonNotFoundException exception = new PokemonNotFoundException(TestConstants.Names.PIKACHU);

        ResponseEntity<Map<String, String>> response = handler.handlePokemonNotFoundException(exception);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(r.getBody())
                            .containsEntry(TestConstants.ErrorMessages.ERROR,
                                    String.format(TestConstants.ErrorMessages.POKEMON_NOT_FOUND_FORMAT,
                                            TestConstants.Names.PIKACHU));
                });
    }

    @Test
    void handleTranslationException_shouldReturnOkStatus() {
        TranslationException exception = new TranslationException(
                TestConstants.ErrorMessages.TRANSLATION_SERVICE_UNAVAILABLE);

        ResponseEntity<Map<String, String>> response = handler.handleTranslationException(exception);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(r.getBody())
                            .containsEntry(TestConstants.ErrorMessages.ERROR,
                                    TestConstants.ErrorMessages.TRANSLATION_UNAVAILABLE);
                });
    }

    @Test
    void handleWebClientResponseException_shouldReturnInternalServerError() {
        WebClientResponseException exception = mock(WebClientResponseException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(exception.getMessage()).thenReturn(TestConstants.ErrorMessages.BAD_REQUEST);

        ResponseEntity<Map<String, String>> response = handler.handleWebClientResponseException(exception);

        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                    assertThat(r.getBody())
                            .containsEntry(TestConstants.ErrorMessages.ERROR,
                                    String.format(TestConstants.ErrorMessages.EXTERNAL_API_ERROR_FORMAT,
                                            "400", "BAD_REQUEST"));
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
                            .containsEntry(TestConstants.ErrorMessages.ERROR,
                                    TestConstants.ErrorMessages.UNEXPECTED_ERROR);
                });
    }
}