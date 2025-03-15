package com.pokemon.dto.pokeapi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlavorTextEntryTest {

    @Test
    void testFlavorTextEntryGettersAndSetters() {
        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText("Sample text");

        FlavorTextEntry.Language language = new FlavorTextEntry.Language();
        language.setName("en");
        entry.setLanguage(language);

        assertThat(entry.getFlavorText()).isEqualTo("Sample text");
        assertThat(entry.getLanguage().getName()).isEqualTo("en");

        FlavorTextEntry entry2 = new FlavorTextEntry();
        entry2.setFlavorText("Sample text");
        FlavorTextEntry.Language language2 = new FlavorTextEntry.Language();
        language2.setName("en");
        entry2.setLanguage(language2);

        assertThat(entry).isEqualTo(entry2);
        assertThat(entry.hashCode()).hasSameHashCodeAs(entry2.hashCode());

        String toString = entry.toString();
        assertThat(toString).contains("en");

        FlavorTextEntry.Language langWithConstructor = new FlavorTextEntry.Language("fr");
        FlavorTextEntry entryWithConstructor = new FlavorTextEntry("Different text", langWithConstructor);

        assertThat(entryWithConstructor.getFlavorText()).isEqualTo("Different text");
        assertThat(entryWithConstructor.getLanguage().getName()).isEqualTo("fr");
    }

    @Test
    void testLanguageGettersAndSetters() {
        FlavorTextEntry.Language language = new FlavorTextEntry.Language();
        language.setName("en");

        assertThat(language.getName()).isEqualTo("en");

        FlavorTextEntry.Language language2 = new FlavorTextEntry.Language();
        language2.setName("en");

        assertThat(language).isEqualTo(language2);
        assertThat(language.hashCode()).hasSameHashCodeAs(language2.hashCode());

        assertThat(language.toString()).contains("en");
    }
}