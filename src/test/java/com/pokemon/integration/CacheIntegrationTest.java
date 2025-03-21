package com.pokemon.integration;

import com.pokemon.PokedexApplication;
import com.pokemon.client.PokeApiClient;
import com.pokemon.client.TranslationApiClient;
import com.pokemon.dto.PokemonResponse;
import com.pokemon.dto.pokeapi.FlavorTextEntry;
import com.pokemon.dto.pokeapi.PokemonSpeciesDto;
import com.pokemon.dto.translator.TranslationResponse;
import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PokedexApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CacheIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private PokeApiClient pokeApiClient;

    @MockBean
    private TranslationApiClient translationApiClient;

    private PokemonSpeciesDto pikachu;
    private TranslationResponse.Contents translationContents;
    private TranslationResponse translationResponse;

    @BeforeEach
    void setUp() {
        cacheManager.getCache(TestConstants.Cache.POKEMON_CACHE).clear();
        cacheManager.getCache(TestConstants.Cache.TRANSLATION_CACHE).clear();

        FlavorTextEntry.Language english = new FlavorTextEntry.Language();
        english.setName(TestConstants.Languages.ENGLISH);

        FlavorTextEntry flavorTextEntry = new FlavorTextEntry();
        flavorTextEntry.setFlavorText(TestConstants.Descriptions.PIKACHU_STANDARD);
        flavorTextEntry.setLanguage(english);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName(TestConstants.Habitats.FOREST);

        pikachu = new PokemonSpeciesDto();
        pikachu.setName(TestConstants.Names.PIKACHU);
        pikachu.setFlavorTextEntries(Collections.singletonList(flavorTextEntry));
        pikachu.setHabitat(habitat);
        pikachu.setLegendary(TestConstants.Legendary.FALSE);

        translationContents = new TranslationResponse.Contents();
        translationContents.setTranslated(TestConstants.Descriptions.PIKACHU_SHAKESPEARE);
        translationContents.setText(TestConstants.Descriptions.PIKACHU_STANDARD);
        translationContents.setTranslation(TestConstants.Translations.SHAKESPEARE);

        TranslationResponse.Success success = new TranslationResponse.Success();
        success.setTotal(1);

        translationResponse = new TranslationResponse();
        translationResponse.setSuccess(success);
        translationResponse.setContents(translationContents);
    }

    @Test
    void pokemonCache_shouldReduceApiCalls() {
        when(pokeApiClient.getPokemonSpecies(TestConstants.Names.PIKACHU)).thenReturn(Mono.just(pikachu));

        webTestClient.get()
                .uri("/pokemon/" + TestConstants.Names.PIKACHU)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PokemonResponse.class);

        verify(pokeApiClient, times(1)).getPokemonSpecies(TestConstants.Names.PIKACHU);
    }

    @Test
    void translationCache_shouldReduceApiCalls() {
        when(pokeApiClient.getPokemonSpecies(TestConstants.Names.PIKACHU)).thenReturn(Mono.just(pikachu));
        when(translationApiClient.translateToShakespeare(anyString())).thenReturn(
                Mono.just(translationContents.getTranslated())
        );

        webTestClient.get()
                .uri("/pokemon/translated/" + TestConstants.Names.PIKACHU)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PokemonResponse.class);

        verify(translationApiClient, times(1)).translateToShakespeare(anyString());
    }
}