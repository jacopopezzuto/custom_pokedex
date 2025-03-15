package com.pokemon.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TranslationExceptionTest {

    @Test
    void constructorWithMessage_shouldSetMessage() {
        String errorMessage = "Translation service unavailable";

        TranslationException exception = new TranslationException(errorMessage);

        assertThat(exception)
                .hasMessage(errorMessage)
                .hasNoCause();
    }

    @Test
    void constructorWithMessageAndCause_shouldSetMessageAndCause() {
        String errorMessage = "Translation service unavailable";
        Throwable cause = new RuntimeException("Connection timeout");

        TranslationException exception = new TranslationException(errorMessage, cause);

        assertThat(exception)
                .hasMessage(errorMessage)
                .hasCause(cause);
    }
}