package com.pokemon.service;

import com.pokemon.client.PokeApiClient;
import com.pokemon.dto.PokemonResponse;
import com.pokemon.dto.pokeapi.FlavorTextEntry;
import com.pokemon.dto.pokeapi.PokemonSpeciesDto;
import com.pokemon.util.TestConstants;
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
        when(pokeApiClient.getPokemonSpecies(TestConstants.Names.MEWTWO)).thenReturn(Mono.just(speciesDto));

        Mono<PokemonResponse> result = pokemonService.getPokemonInfo(TestConstants.Names.MEWTWO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals(TestConstants.Names.MEWTWO) &&
                                response.getDescription().equals(TestConstants.Descriptions.MEWTWO_STANDARD) &&
                                response.getHabitat().equals(TestConstants.Habitats.RARE) &&
                                response.isLegendary() == TestConstants.Legendary.TRUE)
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldTranslateToYodaForLegendaryPokemon() {
        when(pokeApiClient.getPokemonSpecies(TestConstants.Names.MEWTWO)).thenReturn(Mono.just(createMewtwoSpecies()));
        when(translationService.translateToYoda(TestConstants.Descriptions.MEWTWO_STANDARD))
                .thenReturn(Mono.just(TestConstants.Descriptions.MEWTWO_YODA));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo(TestConstants.Names.MEWTWO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals(TestConstants.Names.MEWTWO) &&
                                response.getDescription().equals(TestConstants.Descriptions.MEWTWO_YODA) &&
                                response.getHabitat().equals(TestConstants.Habitats.RARE) &&
                                response.isLegendary())
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldTranslateToYodaForCaveHabitat() {
        PokemonSpeciesDto caveSpecies = new PokemonSpeciesDto();
        caveSpecies.setName(TestConstants.Names.ZUBAT);

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName(TestConstants.Languages.ENGLISH);

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText(TestConstants.Descriptions.ZUBAT_STANDARD);
        entry.setLanguage(english);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName(TestConstants.Habitats.CAVE);

        caveSpecies.setFlavorTextEntries(List.of(entry));
        caveSpecies.setHabitat(habitat);
        caveSpecies.setLegendary(TestConstants.Legendary.FALSE);

        when(pokeApiClient.getPokemonSpecies(TestConstants.Names.ZUBAT)).thenReturn(Mono.just(caveSpecies));
        when(translationService.translateToYoda(TestConstants.Descriptions.ZUBAT_STANDARD))
                .thenReturn(Mono.just(TestConstants.Descriptions.ZUBAT_YODA));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo(TestConstants.Names.ZUBAT);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals(TestConstants.Names.ZUBAT) &&
                                response.getDescription().equals(TestConstants.Descriptions.ZUBAT_YODA) &&
                                response.getHabitat().equals(TestConstants.Habitats.CAVE) &&
                                !response.isLegendary())
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldTranslateToShakespeareForNormalPokemon() {
        PokemonSpeciesDto normalSpecies = new PokemonSpeciesDto();
        normalSpecies.setName(TestConstants.Names.PIKACHU);

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName(TestConstants.Languages.ENGLISH);

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText(TestConstants.Descriptions.PIKACHU_STANDARD);
        entry.setLanguage(english);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName(TestConstants.Habitats.FOREST);

        normalSpecies.setFlavorTextEntries(List.of(entry));
        normalSpecies.setHabitat(habitat);
        normalSpecies.setLegendary(TestConstants.Legendary.FALSE);

        when(pokeApiClient.getPokemonSpecies(TestConstants.Names.PIKACHU)).thenReturn(Mono.just(normalSpecies));
        when(translationService.translateToShakespeare(TestConstants.Descriptions.PIKACHU_STANDARD))
                .thenReturn(Mono.just(TestConstants.Descriptions.PIKACHU_SHAKESPEARE));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo(TestConstants.Names.PIKACHU);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals(TestConstants.Names.PIKACHU) &&
                                response.getDescription().equals(TestConstants.Descriptions.PIKACHU_SHAKESPEARE) &&
                                response.getHabitat().equals(TestConstants.Habitats.FOREST) &&
                                !response.isLegendary())
                .verifyComplete();
    }

    @Test
    void getTranslatedPokemonInfo_shouldFallbackToOriginalDescriptionOnTranslationError() {
        when(pokeApiClient.getPokemonSpecies(TestConstants.Names.MEWTWO)).thenReturn(Mono.just(createMewtwoSpecies()));
        when(translationService.translateToYoda(anyString())).thenReturn(Mono.error(new RuntimeException("Translation failed")));

        Mono<PokemonResponse> result = pokemonService.getTranslatedPokemonInfo(TestConstants.Names.MEWTWO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals(TestConstants.Names.MEWTWO) &&
                                response.getDescription().equals(TestConstants.Descriptions.MEWTWO_STANDARD) &&
                                response.getHabitat().equals(TestConstants.Habitats.RARE) &&
                                response.isLegendary())
                .verifyComplete();
    }

    @Test
    void convertToResponse_shouldHandleNullHabitat() {
        PokemonSpeciesDto speciesDto = new PokemonSpeciesDto();
        speciesDto.setName("unknown");
        speciesDto.setHabitat(null);

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName(TestConstants.Languages.ENGLISH);

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText("Test description");
        entry.setLanguage(english);

        speciesDto.setFlavorTextEntries(List.of(entry));
        speciesDto.setLegendary(TestConstants.Legendary.FALSE);

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

            assertThat(description).isEqualTo(TestConstants.ErrorMessages.NO_DESCRIPTION_AVAILABLE);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    private PokemonSpeciesDto createMewtwoSpecies() {
        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName(TestConstants.Languages.ENGLISH);

        FlavorTextEntry englishEntry = new FlavorTextEntry();
        englishEntry.setFlavorText(TestConstants.Descriptions.MEWTWO_STANDARD);
        englishEntry.setLanguage(english);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName(TestConstants.Habitats.RARE);

        PokemonSpeciesDto species = new PokemonSpeciesDto();
        species.setName(TestConstants.Names.MEWTWO);
        species.setFlavorTextEntries(List.of(englishEntry));
        species.setHabitat(habitat);
        species.setLegendary(TestConstants.Legendary.TRUE);

        return species;
    }
}