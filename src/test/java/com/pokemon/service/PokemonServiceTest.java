package com.pokemon.service;

import com.pokemon.client.PokeApiClient;
import com.pokemon.dto.PokemonResponse;
import com.pokemon.dto.pokeapi.FlavorTextEntry;
import com.pokemon.dto.pokeapi.PokemonSpeciesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokeApiClient pokeApiClient;

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private PokemonService pokemonService;

    @Test
    void getPokemonInfo_shouldReturnCorrectInfo() {
        PokemonSpeciesDto speciesDto = createMewtwoSpecies();
        when(pokeApiClient.getPokemonSpecies("mewtwo")).thenReturn(Mono.just(speciesDto));

        Mono<PokemonResponse> result = pokemonService.getPokemonInfo("mewtwo");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals("mewtwo") &&
                                response.getDescription().equals("It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.") &&
                                response.getHabitat().equals("rare") &&
                                response.isLegendary())
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldTranslateToYodaForLegendaryPokemon() {
        String originalDescription = "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.";
        String yodaTranslation = "Created by a scientist after years of horrific gene splicing and dna engineering experiments, it was.";

        when(pokeApiClient.getPokemonSpecies("mewtwo")).thenReturn(Mono.just(createMewtwoSpecies()));
        when(translationService.translateToYoda(originalDescription)).thenReturn(Mono.just(yodaTranslation));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo("mewtwo");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals("mewtwo") &&
                                response.getDescription().equals(yodaTranslation) &&
                                response.getHabitat().equals("rare") &&
                                response.isLegendary())
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldTranslateToYodaForCaveHabitat() {
        PokemonSpeciesDto caveSpecies = new PokemonSpeciesDto();
        caveSpecies.setName("zubat");

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName("en");

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText("Forms colonies in perpetually dark places and uses ultrasonic waves to identify and approach targets.");
        entry.setLanguage(english);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName("cave");

        caveSpecies.setFlavorTextEntries(List.of(entry));
        caveSpecies.setHabitat(habitat);
        caveSpecies.setLegendary(false);

        String originalDescription = "Forms colonies in perpetually dark places and uses ultrasonic waves to identify and approach targets.";
        String yodaTranslation = "Forms colonies in perpetually dark places, it does. Uses ultrasonic waves to identify and approach targets, it does.";

        when(pokeApiClient.getPokemonSpecies("zubat")).thenReturn(Mono.just(caveSpecies));
        when(translationService.translateToYoda(originalDescription)).thenReturn(Mono.just(yodaTranslation));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo("zubat");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals("zubat") &&
                                response.getDescription().equals(yodaTranslation) &&
                                response.getHabitat().equals("cave") &&
                                !response.isLegendary())
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldTranslateToShakespeareForNormalPokemon() {
        PokemonSpeciesDto normalSpecies = new PokemonSpeciesDto();
        normalSpecies.setName("pikachu");

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName("en");

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText("When several of these Pokémon gather, their electricity could build and cause lightning storms.");
        entry.setLanguage(english);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName("forest");

        normalSpecies.setFlavorTextEntries(List.of(entry));
        normalSpecies.setHabitat(habitat);
        normalSpecies.setLegendary(false);

        String originalDescription = "When several of these Pokémon gather, their electricity could build and cause lightning storms.";
        String shakespeareTranslation = "When several of these pokémon gather, their electricity couldst buildeth and cause lightning storms.";

        when(pokeApiClient.getPokemonSpecies("pikachu")).thenReturn(Mono.just(normalSpecies));
        when(translationService.translateToShakespeare(originalDescription)).thenReturn(Mono.just(shakespeareTranslation));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo("pikachu");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals("pikachu") &&
                                response.getDescription().equals(shakespeareTranslation) &&
                                response.getHabitat().equals("forest") &&
                                !response.isLegendary())
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldFallbackToOriginalDescriptionOnTranslationError() {
        String originalDescription = "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.";

        when(pokeApiClient.getPokemonSpecies("mewtwo")).thenReturn(Mono.just(createMewtwoSpecies()));
        when(translationService.translateToYoda(anyString())).thenReturn(Mono.error(new RuntimeException("Translation failed")));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo("mewtwo");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals("mewtwo") &&
                                response.getDescription().equals(originalDescription) &&
                                response.getHabitat().equals("rare") &&
                                response.isLegendary())
                .verifyComplete();
    }

    @Test
    void convertToResponse_shouldHandleNullHabitat() {
        PokemonSpeciesDto speciesDto = new PokemonSpeciesDto();
        speciesDto.setName("unknown");
        speciesDto.setHabitat(null);

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName("en");

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText("Test description");
        entry.setLanguage(english);

        speciesDto.setFlavorTextEntries(List.of(entry));
        speciesDto.setLegendary(false);

        try {
            Method convertMethod = PokemonService.class.getDeclaredMethod("convertToResponse", PokemonSpeciesDto.class);
            convertMethod.setAccessible(true);

            PokemonResponse response = (PokemonResponse) convertMethod.invoke(pokemonService, speciesDto);

            assertThat(response.getHabitat()).isEqualTo("unknown");
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void extractEnglishDescription_shouldHandleEmptyFlavorTextEntries() {
        PokemonSpeciesDto speciesDto = new PokemonSpeciesDto();
        speciesDto.setName("test");
        speciesDto.setFlavorTextEntries(new ArrayList<>());

        try {
            Method extractMethod = PokemonService.class.getDeclaredMethod("extractEnglishDescription", PokemonSpeciesDto.class);
            extractMethod.setAccessible(true);

            String description = (String) extractMethod.invoke(pokemonService, speciesDto);

            assertThat(description).isEqualTo("No description available");
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    private PokemonSpeciesDto createMewtwoSpecies() {
        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName("en");

        FlavorTextEntry englishEntry = new FlavorTextEntry();
        englishEntry.setFlavorText("It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.");
        englishEntry.setLanguage(english);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName("rare");

        PokemonSpeciesDto species = new PokemonSpeciesDto();
        species.setName("mewtwo");
        species.setFlavorTextEntries(List.of(englishEntry));
        species.setHabitat(habitat);
        species.setLegendary(true);

        return species;
    }
}