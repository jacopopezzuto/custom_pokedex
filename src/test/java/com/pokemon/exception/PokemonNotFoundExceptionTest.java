package com.pokemon.exception;

import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonNotFoundExceptionTest {

    @Test
    void constructor_shouldFormatMessageCorrectly() {
        String pokemonName = TestConstants.Names.PIKACHU;

        PokemonNotFoundException exception = new PokemonNotFoundException(pokemonName);

        assertThat(exception)
                .hasMessage(String.format(TestConstants.ErrorMessages.POKEMON_NOT_FOUND_FORMAT,
                        TestConstants.Names.PIKACHU));
    }
}