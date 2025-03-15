package com.pokemon.dto.translator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TranslationResponseTest {

    @Test
    void testTranslationResponseGettersAndSetters() {
        TranslationResponse response = new TranslationResponse();

        TranslationResponse.Success success = new TranslationResponse.Success();
        success.setTotal(1);

        TranslationResponse.Contents contents = new TranslationResponse.Contents();
        contents.setTranslated("Translated text");
        contents.setText("Original text");
        contents.setTranslation("yoda");

        response.setSuccess(success);
        response.setContents(contents);

        assertThat(response)
                .extracting(
                        r -> r.getSuccess().getTotal(),
                        r -> r.getContents().getTranslated(),
                        r -> r.getContents().getText(),
                        r -> r.getContents().getTranslation())
                .containsExactly(1, "Translated text", "Original text", "yoda");

        TranslationResponse response2 = new TranslationResponse();

        TranslationResponse.Success success2 = new TranslationResponse.Success();
        success2.setTotal(1);

        TranslationResponse.Contents contents2 = new TranslationResponse.Contents();
        contents2.setTranslated("Translated text");
        contents2.setText("Original text");
        contents2.setTranslation("yoda");

        response2.setSuccess(success2);
        response2.setContents(contents2);

        assertThat(response)
                .isEqualTo(response2)
                .hasSameHashCodeAs(response2);

        assertThat(response.toString())
                .contains("Translated text", "Original text", "yoda");

        TranslationResponse.Success successWithConstructor = new TranslationResponse.Success(2);
        TranslationResponse.Contents contentsWithConstructor =
                new TranslationResponse.Contents("New translated", "New original", "shakespeare");
        TranslationResponse responseWithConstructor =
                new TranslationResponse(successWithConstructor, contentsWithConstructor);

        assertThat(responseWithConstructor)
                .satisfies(r -> {
                    assertThat(r.getSuccess().getTotal()).isEqualTo(2);
                    assertThat(r.getContents().getTranslated()).isEqualTo("New translated");
                    assertThat(r.getContents().getText()).isEqualTo("New original");
                    assertThat(r.getContents().getTranslation()).isEqualTo("shakespeare");
                });
    }

    @Test
    void testSuccessGettersAndSetters() {
        TranslationResponse.Success success = new TranslationResponse.Success();
        success.setTotal(5);

        assertThat(success.getTotal()).isEqualTo(5);

        TranslationResponse.Success success2 = new TranslationResponse.Success();
        success2.setTotal(5);

        assertThat(success)
                .isEqualTo(success2)
                .hasSameHashCodeAs(success2);

        assertThat(success.toString()).contains("5");

        TranslationResponse.Success successWithConstructor = new TranslationResponse.Success(10);
        assertThat(successWithConstructor.getTotal()).isEqualTo(10);
    }

    @Test
    void testContentsGettersAndSetters() {
        TranslationResponse.Contents contents = new TranslationResponse.Contents();
        contents.setTranslated("Translated text");
        contents.setText("Original text");
        contents.setTranslation("yoda");

        assertThat(contents)
                .extracting(
                        TranslationResponse.Contents::getTranslated,
                        TranslationResponse.Contents::getText,
                        TranslationResponse.Contents::getTranslation)
                .containsExactly("Translated text", "Original text", "yoda");

        TranslationResponse.Contents contents2 = new TranslationResponse.Contents();
        contents2.setTranslated("Translated text");
        contents2.setText("Original text");
        contents2.setTranslation("yoda");

        assertThat(contents)
                .isEqualTo(contents2)
                .hasSameHashCodeAs(contents2);

        assertThat(contents.toString())
                .contains("Translated text", "Original text", "yoda");

        TranslationResponse.Contents contentsWithConstructor =
                new TranslationResponse.Contents("New translated", "New original", "shakespeare");

        assertThat(contentsWithConstructor)
                .extracting(
                        TranslationResponse.Contents::getTranslated,
                        TranslationResponse.Contents::getText,
                        TranslationResponse.Contents::getTranslation)
                .containsExactly("New translated", "New original", "shakespeare");
    }
}