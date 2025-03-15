package com.pokemon.client;

import com.pokemon.dto.pokeapi.PokemonSpeciesDto;
import com.pokemon.exception.PokemonNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PokeApiClient {

    private final WebClient webClient;

    @Value("${api.poke-api.base-url}")
    private String baseUrl;

    @Value("${api.poke-api.species-path}")
    private String speciesPath;

    /**
     * Fetches Pokemon species information by name.
     *
     * @param name the Pokemon name
     * @return Pokemon species data
     * @throws PokemonNotFoundException if the Pokemon is not found
     */
    public Mono<PokemonSpeciesDto> getPokemonSpecies(String name) {
        String pokemonName = name.toLowerCase();
        String url = baseUrl + speciesPath;

        log.debug("Fetching Pokemon species data for: {}", pokemonName);

        return webClient.get()
                .uri(url, pokemonName)
                .retrieve()
                .bodyToMono(PokemonSpeciesDto.class)
                .doOnSuccess(data -> log.debug("Successfully retrieved Pokemon species data for: {}", pokemonName))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        log.error("Pokemon not found with name: {}", pokemonName);
                        return Mono.error(new PokemonNotFoundException(pokemonName));
                    }
                    log.error("Error fetching Pokemon data: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }
}