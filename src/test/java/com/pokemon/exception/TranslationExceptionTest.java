package com.pokemon.exception;

import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TranslationExceptionTest {

    @Test
    void constructorWithMessage_shouldSetMessage() {
        String errorMessage = TestConstants.ErrorMessages.TRANSLATION_SERVICE_UNAVAILABLE;

        TranslationException exception = new TranslationException(errorMessage);

        assertThat(exception)
                .hasMessage(errorMessage)
                .hasNoCause();
    }

    @Test
    void constructorWithMessageAndCause_shouldSetMessageAndCause() {
        Throwable cause = new RuntimeException(TestConstants.ErrorMessages.CONNECTION_TIMEOUT);

        TranslationException exception = new TranslationException(
                TestConstants.ErrorMessages.TRANSLATION_SERVICE_UNAVAILABLE, cause);

        assertThat(exception)
                .hasMessage(TestConstants.ErrorMessages.TRANSLATION_SERVICE_UNAVAILABLE)
                .hasCause(cause);
    }
}