package com.pokemon.constants;

import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppConstantsTest {

    @ParameterizedTest
    @MethodSource("provideConstantsClasses")
    void shouldNotBeAbleToInstantiateConstantsClasses(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(AssertionError.class);
    }

    private static Stream<Arguments> provideConstantsClasses() {
        return Stream.of(
                Arguments.of(AppConstants.class),
                Arguments.of(AppConstants.Cache.class),
                Arguments.of(AppConstants.Api.class),
                Arguments.of(AppConstants.WebClient.class),
                Arguments.of(AppConstants.Language.class)
        );
    }

    @Test
    void constantsShouldHaveCorrectValues() {
        assertThat(TestConstants.Cache.POKEMON_CACHE).isEqualTo(AppConstants.Cache.POKEMON_CACHE);
        assertThat(TestConstants.Cache.TRANSLATION_CACHE).isEqualTo(AppConstants.Cache.TRANSLATION_CACHE);

        assertThat(TestConstants.Translations.SHAKESPEARE).isEqualTo(AppConstants.Api.SHAKESPEARE_TRANSLATION);
        assertThat(TestConstants.Translations.YODA).isEqualTo(AppConstants.Api.YODA_TRANSLATION);
        assertThat(TestConstants.Habitats.CAVE).isEqualTo(AppConstants.Api.CAVE_HABITAT);

        assertThat(TestConstants.Config.DEFAULT_TIMEOUT_MS).isEqualTo(AppConstants.WebClient.DEFAULT_TIMEOUT_MS);
        assertThat(TestConstants.Config.DEFAULT_MAX_MEMORY_SIZE).isEqualTo(AppConstants.WebClient.DEFAULT_MAX_MEMORY_SIZE);

        assertThat(TestConstants.Languages.ENGLISH).isEqualTo(AppConstants.Language.ENGLISH);
    }
}