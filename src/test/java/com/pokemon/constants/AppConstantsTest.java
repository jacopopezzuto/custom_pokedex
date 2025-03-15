package com.pokemon.constants;

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
        assertThat(AppConstants.Cache.POKEMON_CACHE).isEqualTo("pokemonCache");
        assertThat(AppConstants.Cache.TRANSLATION_CACHE).isEqualTo("translationCache");

        assertThat(AppConstants.Api.SHAKESPEARE_TRANSLATION).isEqualTo("shakespeare");
        assertThat(AppConstants.Api.YODA_TRANSLATION).isEqualTo("yoda");
        assertThat(AppConstants.Api.CAVE_HABITAT).isEqualTo("cave");

        assertThat(AppConstants.WebClient.DEFAULT_TIMEOUT_MS).isEqualTo(5000);
        assertThat(16 * 1024 * 1024).isEqualTo(AppConstants.WebClient.DEFAULT_MAX_MEMORY_SIZE);

        assertThat(AppConstants.Language.ENGLISH).isEqualTo("en");
    }
}