package com.pokemon.dto.pokeapi;

import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonSpeciesDtoTest {

    @Test
    void testPokemonSpeciesDtoGettersAndSetters() {
        PokemonSpeciesDto dto = new PokemonSpeciesDto();

        dto.setName(TestConstants.Names.PIKACHU);

        FlavorTextEntry entry1 = new FlavorTextEntry();
        entry1.setFlavorText(TestConstants.Descriptions.PIKACHU_STANDARD);
        FlavorTextEntry.Language lang1 = new FlavorTextEntry.Language();
        lang1.setName(TestConstants.Languages.ENGLISH);
        entry1.setLanguage(lang1);

        FlavorTextEntry entry2 = new FlavorTextEntry();
        entry2.setFlavorText(TestConstants.Descriptions.PIKACHU_FRENCH);
        FlavorTextEntry.Language lang2 = new FlavorTextEntry.Language();
        lang2.setName(TestConstants.Languages.FRENCH);
        entry2.setLanguage(lang2);

        List<FlavorTextEntry> entries = Arrays.asList(entry1, entry2);
        dto.setFlavorTextEntries(entries);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
        habitat.setName(TestConstants.Habitats.FOREST);
        dto.setHabitat(habitat);

        dto.setLegendary(TestConstants.Legendary.FALSE);

        assertThat(dto)
                .extracting(
                        PokemonSpeciesDto::getName,
                        PokemonSpeciesDto::getFlavorTextEntries,
                        d -> d.getHabitat().getName(),
                        PokemonSpeciesDto::isLegendary
                )
                .containsExactly(
                        TestConstants.Names.PIKACHU,
                        entries,
                        TestConstants.Habitats.FOREST,
                        TestConstants.Legendary.FALSE
                );

        PokemonSpeciesDto dto2 = new PokemonSpeciesDto();
        dto2.setName(TestConstants.Names.PIKACHU);
        dto2.setFlavorTextEntries(entries);
        dto2.setHabitat(habitat);
        dto2.setLegendary(TestConstants.Legendary.FALSE);

        assertThat(dto)
                .isEqualTo(dto2)
                .hasSameHashCodeAs(dto2);

        assertThat(dto.toString())
                .contains(TestConstants.Names.PIKACHU, TestConstants.Habitats.FOREST, String.valueOf(TestConstants.Legendary.FALSE));
    }

    @Test
    void testAllArgsConstructor() {
        String name = TestConstants.Names.MEWTWO;

        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText(TestConstants.Descriptions.PIKACHU_STANDARD);
        FlavorTextEntry.Language lang = new FlavorTextEntry.Language();
        lang.setName(TestConstants.Languages.ENGLISH);
        entry.setLanguage(lang);

        List<FlavorTextEntry> entries = List.of(entry);

        PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat(TestConstants.Habitats.RARE);
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
        habitat1.setName(TestConstants.Habitats.CAVE);
        assertThat(habitat1.getName()).isEqualTo(TestConstants.Habitats.CAVE);

        PokemonSpeciesDto.Habitat habitat2 = new PokemonSpeciesDto.Habitat(TestConstants.Habitats.FOREST);
        assertThat(habitat2.getName()).isEqualTo(TestConstants.Habitats.FOREST);

        PokemonSpeciesDto.Habitat habitat3 = new PokemonSpeciesDto.Habitat(TestConstants.Habitats.CAVE);
        assertThat(habitat1)
                .isEqualTo(habitat3)
                .hasSameHashCodeAs(habitat3)
                .isNotEqualTo(habitat2);

        assertThat(habitat1.toString()).contains(TestConstants.Habitats.CAVE);
        assertThat(habitat2.toString()).contains(TestConstants.Habitats.FOREST);
    }
}