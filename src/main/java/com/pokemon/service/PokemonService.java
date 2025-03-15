package com.pokemon.service;

import com.pokemon.client.PokeApiClient;
import com.pokemon.constants.AppConstants;
import com.pokemon.dto.PokemonResponse;
import com.pokemon.dto.pokeapi.FlavorTextEntry;
import com.pokemon.dto.pokeapi.PokemonSpeciesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PokeApiClient pokeApiClient;
    private final TranslationService translationService;

    public Mono<PokemonResponse> getPokemonInfo(String name) {
        return pokeApiClient.getPokemonSpecies(name.toLowerCase())
                .map(this::convertToResponse)
                .doOnSuccess(response -> log.debug("Successfully retrieved Pokemon info for: {}", name))
                .doOnError(error -> log.error("Error retrieving Pokemon info for: {}", name, error));
    }

    public Mono<PokemonResponse> getTranslatedPokemonInfo(String name) {
        return getPokemonInfo(name)
                .flatMap(pokemon -> {
                    if (AppConstants.Api.CAVE_HABITAT.equalsIgnoreCase(pokemon.getHabitat())
                            || pokemon.isLegendary()) {
                        return applyYodaTranslation(pokemon);
                    } else {
                        return applyShakespeareTranslation(pokemon);
                    }
                });
    }

    private Mono<PokemonResponse> applyYodaTranslation(PokemonResponse pokemon) {
        return translationService.translateToYoda(pokemon.getDescription())
                .map(translatedText -> {
                    pokemon.setDescription(translatedText);
                    return pokemon;
                })
                .onErrorReturn(pokemon);
    }

    private Mono<PokemonResponse> applyShakespeareTranslation(PokemonResponse pokemon) {
        return translationService.translateToShakespeare(pokemon.getDescription())
                .map(translatedText -> {
                    pokemon.setDescription(translatedText);
                    return pokemon;
                })
                .onErrorReturn(pokemon);
    }

    private PokemonResponse convertToResponse(PokemonSpeciesDto speciesDto) {
        return PokemonResponse.builder()
                .name(speciesDto.getName())
                .description(extractEnglishDescription(speciesDto))
                .habitat(speciesDto.getHabitat() != null ? speciesDto.getHabitat().getName() : "unknown")
                .isLegendary(speciesDto.isLegendary())
                .build();
    }

    private String extractEnglishDescription(PokemonSpeciesDto speciesDto) {
        return speciesDto.getFlavorTextEntries().stream()
                .filter(entry -> AppConstants.Language.ENGLISH.equals(entry.getLanguage().getName()))
                .findFirst()
                .map(FlavorTextEntry::getFlavorText)
                .map(this::cleanDescription)
                .orElse("No description available");
    }

    private String cleanDescription(String description) {
        return description.replaceAll("[\\n\\f\\r]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}