package com.pokemon.dto.pokeapi;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonSpeciesDtoTest {

    @Test
    void testPokemonSpeciesDtoGettersAndSetters() {
        PokemonSpeciesDto dto = new PokemonSpeciesDto();

        dto.setName("pikachu");

        FlavorTextEntry entry1 = new FlavorTextEntry();
        entry1.setFlavorText("Description 1");
        FlavorTextEntry.Language lang1 = new FlavorTextEntry.Language();
        lang1.setName("en");
        entry1.setLanguage(lang1);

        FlavorTextEntry entry2 = new FlavorTextEntry();
        entry2.setFlavorText("Description 2");
        FlavorTextEntry.Language lang2 = new FlavorTextEntry.Language();
        lang2.setName("fr");
        entry2.setLanguage(lang2);

        List<FlavorTextEntry> entries = Arrays.asList(entry1, entry2);
        dto.setFlavorTextEntries(entries);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName("forest");
        dto.setHabitat(habitat);

        dto.setLegendary(false);

        assertThat(dto)
                .extracting(
                        PokemonSpeciesDto::getName,
                        PokemonSpeciesDto::getFlavorTextEntries,
                        d -> d.getHabitat().getName(),
                        PokemonSpeciesDto::isLegendary
                )
                .containsExactly(
                        "pikachu",
                        entries,
                        "forest",
                        false
                );

        PokemonSpeciesDto dto2 = new PokemonSpeciesDto();
        dto2.setName("pikachu");
        dto2.setFlavorTextEntries(entries);
        dto2.setHabitat(habitat);
        dto2.setLegendary(false);

        assertThat(dto)
                .isEqualTo(dto2)
                .hasSameHashCodeAs(dto2);

        assertThat(dto.toString())
                .contains("pikachu", "forest", "false");
    }

    @Test
    void testAllArgsConstructor() {
        String name = "mewtwo";

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText("Description");
        FlavorTextEntry.Language lang = new FlavorTextEntry.Language();
        lang.setName("en");
        entry.setLanguage(lang);

        List<FlavorTextEntry> entries = List.of(entry);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat("rare");
        boolean isLegendary = true;

        PokemonSpeciesDto dto = new PokemonSpeciesDto(name, entries, habitat, isLegendary);

        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getFlavorTextEntries()).isSameAs(entries);
        assertThat(dto.getHabitat()).isSameAs(habitat);
        assertThat(dto.isLegendary()).isEqualTo(isLegendary);

        PokemonSpeciesDto dtoWithSetters = new PokemonSpeciesDto();
        dtoWithSetters.setName(name);
        dtoWithSetters.setFlavorTextEntries(entries);
        dtoWithSetters.setHabitat(habitat);
        dtoWithSetters.setLegendary(isLegendary);

        assertThat(dto).isEqualTo(dtoWithSetters);
    }

    @Test
    void testHabitatClass() {
        PokemonSpeciesDto.Habitat habitat1 = new PokemonSpeciesDto.Habitat();
        habitat1.setName("cave");
        assertThat(habitat1.getName()).isEqualTo("cave");

        PokemonSpeciesDto.Habitat habitat2 = new PokemonSpeciesDto.Habitat("mountain");
        assertThat(habitat2.getName()).isEqualTo("mountain");

        PokemonSpeciesDto.Habitat habitat3 = new PokemonSpeciesDto.Habitat("cave");
        assertThat(habitat1)
                .isEqualTo(habitat3)
                .hasSameHashCodeAs(habitat3)
                .isNotEqualTo(habitat2);

        assertThat(habitat1.toString()).contains("cave");
        assertThat(habitat2.toString()).contains("mountain");
    }
}