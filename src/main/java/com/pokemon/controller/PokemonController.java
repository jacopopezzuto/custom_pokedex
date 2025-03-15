package com.pokemon.controller;

import com.pokemon.dto.PokemonResponse;
import com.pokemon.service.PokemonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    /**
     * Get basic Pokemon information.
     *
     * @param name Pokemon name
     * @return Pokemon information
     */
    @GetMapping("/{name}")
    public Mono<ResponseEntity<PokemonResponse>> getPokemon(@PathVariable String name) {
        log.info("Fetching information for Pokemon: {}", name);
        return pokemonService.getPokemonInfo(name)
                .map(ResponseEntity::ok);
    }

    /**
     * Get Pokemon information with translated description.
     *
     * @param name Pokemon name
     * @return Pokemon information with translated description
     */
    @GetMapping("/translated/{name}")
    public Mono<ResponseEntity<PokemonResponse>> getTranslatedPokemon(@PathVariable String name) {
        log.info("Fetching translated information for Pokemon: {}", name);
        return pokemonService.getTranslatedPokemonInfo(name)
                .map(ResponseEntity::ok);
    }
}