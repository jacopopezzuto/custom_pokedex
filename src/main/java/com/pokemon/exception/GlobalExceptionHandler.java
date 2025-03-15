package com.pokemon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PokemonNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePokemonNotFoundException(PokemonNotFoundException ex) {
        log.error("Pokemon not found: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TranslationException.class)
    public ResponseEntity<Map<String, String>> handleTranslationException(TranslationException ex) {
        log.warn("Translation failed: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Could not apply translation. Using standard description instead.");
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, String>> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("API error: {} - {}", ex.getStatusCode(), ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "External API error: " + ex.getStatusCode());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
