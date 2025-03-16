package com.pokemon.dto.translator;

import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TranslationResponseTest {

    @Test
    void testTranslationResponseGettersAndSetters() {
        TranslationResponse response = new TranslationResponse();

        TranslationResponse.Success success = new TranslationResponse.Success();
        success.setTotal(1);

        TranslationResponse.Contents contents = new TranslationResponse.Contents();
        contents.setTranslated(TestConstants.Descriptions.ZUBAT_YODA);
        contents.setText(TestConstants.Descriptions.ZUBAT_STANDARD);
        contents.setTranslation(TestConstants.Translations.YODA);
        response.setSuccess(success);
        response.setContents(contents);

        assertThat(response)
                .extracting(
                        r -> r.getSuccess().getTotal(),
                        r -> r.getContents().getTranslated(),
                        r -> r.getContents().getText(),
                        r -> r.getContents().getTranslation())
                .containsExactly(1, TestConstants.Descriptions.ZUBAT_YODA, TestConstants.Descriptions.ZUBAT_STANDARD, TestConstants.Translations.YODA);

        TranslationResponse response2 = new TranslationResponse();

        TranslationResponse.Success success2 = new TranslationResponse.Success();
        success2.setTotal(1);

        TranslationResponse.Contents contents2 = new TranslationResponse.Contents();
        contents2.setTranslated(TestConstants.Descriptions.ZUBAT_YODA);
        contents2.setText(TestConstants.Descriptions.ZUBAT_STANDARD);
        contents2.setTranslation(TestConstants.Translations.YODA);

        response2.setSuccess(success2);
        response2.setContents(contents2);

        assertThat(response)
                .isEqualTo(response2)
                .hasSameHashCodeAs(response2);

        assertThat(response.toString())
                .contains(TestConstants.Descriptions.ZUBAT_YODA, TestConstants.Descriptions.ZUBAT_STANDARD, TestConstants.Translations.YODA);

        TranslationResponse.Success successWithConstructor = new TranslationResponse.Success(2);
        TranslationResponse.Contents contentsWithConstructor =
                new TranslationResponse.Contents(TestConstants.Descriptions.PIKACHU_SHAKESPEARE,
                        TestConstants.Descriptions.PIKACHU_STANDARD,
                        TestConstants.Translations.SHAKESPEARE);
        TranslationResponse responseWithConstructor =
                new TranslationResponse(successWithConstructor, contentsWithConstructor);

        assertThat(responseWithConstructor)
                .satisfies(r -> {
                    assertThat(r.getSuccess().getTotal()).isEqualTo(2);
                    assertThat(r.getContents().getTranslated()).isEqualTo(TestConstants.Descriptions.PIKACHU_SHAKESPEARE);
                    assertThat(r.getContents().getText()).isEqualTo(TestConstants.Descriptions.PIKACHU_STANDARD);
                    assertThat(r.getContents().getTranslation()).isEqualTo(TestConstants.Translations.SHAKESPEARE);
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
        contents.setTranslated(TestConstants.Descriptions.PIKACHU_SHAKESPEARE);
        contents.setText(TestConstants.Descriptions.PIKACHU_STANDARD);
        contents.setTranslation(TestConstants.Translations.SHAKESPEARE);

        assertThat(contents)
                .extracting(
                        TranslationResponse.Contents::getTranslated,
                        TranslationResponse.Contents::getText,
                        TranslationResponse.Contents::getTranslation)
                .containsExactly(TestConstants.Descriptions.PIKACHU_SHAKESPEARE,
                        TestConstants.Descriptions.PIKACHU_STANDARD,
                        TestConstants.Translations.SHAKESPEARE);

        TranslationResponse.Contents contents2 = new TranslationResponse.Contents();
        contents2.setTranslated(TestConstants.Descriptions.PIKACHU_SHAKESPEARE);
        contents2.setText(TestConstants.Descriptions.PIKACHU_STANDARD);
        contents2.setTranslation(TestConstants.Translations.SHAKESPEARE);

        assertThat(contents)
                .isEqualTo(contents2)
                .hasSameHashCodeAs(contents2);

        assertThat(contents.toString())
                .contains(TestConstants.Descriptions.PIKACHU_SHAKESPEARE,
                        TestConstants.Descriptions.PIKACHU_STANDARD,
                        TestConstants.Translations.SHAKESPEARE);

        TranslationResponse.Contents contentsWithConstructor =
                new TranslationResponse.Contents(TestConstants.Descriptions.ZUBAT_YODA,
                        TestConstants.Descriptions.ZUBAT_STANDARD,
                        TestConstants.Translations.YODA);

        assertThat(contentsWithConstructor)
                .extracting(
                        TranslationResponse.Contents::getTranslated,
                        TranslationResponse.Contents::getText,
                        TranslationResponse.Contents::getTranslation)
                .containsExactly(TestConstants.Descriptions.ZUBAT_YODA,
                        TestConstants.Descriptions.ZUBAT_STANDARD,
                        TestConstants.Translations.YODA);
    }
}