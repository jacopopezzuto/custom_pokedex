package com.pokemon.dto.pokeapi;

import com.pokemon.util.TestConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlavorTextEntryTest {

    @Test
    void testFlavorTextEntryGettersAndSetters() {
        FlavorTextEntry entry = new FlavorTextEntry();
        entry.setFlavorText(TestConstants.Descriptions.PIKACHU_STANDARD);

        FlavorTextEntry.Language language = new FlavorTextEntry.Language();
        language.setName(TestConstants.Languages.ENGLISH);
        entry.setLanguage(language);

        assertThat(entry.getFlavorText()).isEqualTo(TestConstants.Descriptions.PIKACHU_STANDARD);
        assertThat(entry.getLanguage().getName()).isEqualTo(TestConstants.Languages.ENGLISH);

        FlavorTextEntry entry2 = new FlavorTextEntry();
        entry2.setFlavorText(TestConstants.Descriptions.PIKACHU_STANDARD);
        FlavorTextEntry.Language language2 = new FlavorTextEntry.Language();
        language2.setName(TestConstants.Languages.ENGLISH);
        entry2.setLanguage(language2);

        assertThat(entry).isEqualTo(entry2);
        assertThat(entry.hashCode()).hasSameHashCodeAs(entry2.hashCode());

        String toString = entry.toString();
        assertThat(toString).contains(TestConstants.Languages.ENGLISH);

        FlavorTextEntry.Language langWithConstructor = new FlavorTextEntry.Language(TestConstants.Languages.FRENCH);
        FlavorTextEntry entryWithConstructor = new FlavorTextEntry(TestConstants.Descriptions.PIKACHU_SHAKESPEARE, langWithConstructor);

        assertThat(entryWithConstructor.getFlavorText()).isEqualTo(TestConstants.Descriptions.PIKACHU_SHAKESPEARE);
        assertThat(entryWithConstructor.getLanguage().getName()).isEqualTo(TestConstants.Languages.FRENCH);
    }

    @Test
    void testLanguageGettersAndSetters() {
        FlavorTextEntry.Language language = new FlavorTextEntry.Language();
        language.setName(TestConstants.Languages.ENGLISH);

        assertThat(language.getName()).isEqualTo(TestConstants.Languages.ENGLISH);

        FlavorTextEntry.Language language2 = new FlavorTextEntry.Language();
        language2.setName(TestConstants.Languages.ENGLISH);

        assertThat(language).isEqualTo(language2);
        assertThat(language.hashCode()).hasSameHashCodeAs(language2.hashCode());

        assertThat(language.toString()).contains(TestConstants.Languages.ENGLISH);
    }
}