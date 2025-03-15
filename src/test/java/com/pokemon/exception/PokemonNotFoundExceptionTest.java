package com.pokemon.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonNotFoundExceptionTest {

    @Test
    void constructor_shouldFormatMessageCorrectly() {
        String pokemonName = "pikachu";

        PokemonNotFoundException exception = new PokemonNotFoundException(pokemonName);

        assertThat(exception)
                .hasMessage("Pokemon not found with name: pikachu");
    }
}