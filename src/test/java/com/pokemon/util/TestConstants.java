package com.pokemon.util;

import com.pokemon.dto.pokeapi.FlavorTextEntry;
import com.pokemon.dto.pokeapi.PokemonSpeciesDto;
import com.pokemon.dto.translator.TranslationResponse;

import java.util.List;

public final class TestConstants {

    private TestConstants() {}

    public static final class Names {
        public static final String MEWTWO = "mewtwo";
        public static final String PIKACHU = "pikachu";
        public static final String ZUBAT = "zubat";
        public static final String NONEXISTENT_POKEMON = "nonexistent";
    }

    public static final class Descriptions {
        public static final String PIKACHU_STANDARD =
                "When several of these POKéMON gather, their electricity could build and cause lightning storms.";
        public static final String PIKACHU_FRENCH =
                "Ce Pokémon dispose de petites poches dans les joues pour\\nstocker de l'électricité. Elles semblent se charger pendant\\nque Pikachu dort. Il libère parfois un peu d'électricité\\nlorsqu'il n'est pas encore bien réveillé.";
        public static final String PIKACHU_SHAKESPEARE =
                "At which hour several of these pokémon gather, their electricity couldst buildeth and cause lightning storms.";
        public static final String ZUBAT_STANDARD =
                "Forms colonies in perpetually dark places. Uses ultrasonic waves to identify and approach targets.";
        public static final String ZUBAT_YODA =
                "Forms colonies in perpetually dark places. Ultrasonic waves to identify and approach targets, uses.";

        public static final String MEWTWO_STANDARD =
                "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.";
        public static final String MEWTWO_YODA =
                "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was.";
    }

    public static final class Habitats {
        public static final String RARE = "rare";
        public static final String FOREST = "forest";
        public static final String CAVE = "cave";
    }

    public static final class Legendary {
        public static final boolean TRUE = true;
        public static final boolean FALSE = false;
    }

    public static final class Cache {
        public static final String POKEMON_CACHE = "pokemonCache";
        public static final String TRANSLATION_CACHE = "translationCache";
    }

    public static final class Translations {
        public static final String SHAKESPEARE = "shakespeare";
        public static final String YODA = "yoda";
    }

    public static final class Languages {
        public static final String ENGLISH = "en";
        public static final String FRENCH = "fr";
    }

    public static final class Config {
        public static final int DEFAULT_TIMEOUT_MS = 5000;
        public static final int DEFAULT_MAX_MEMORY_SIZE = 16 * 1024 * 1024;
    }

    public static final class ErrorMessages {
        public static final String POKEMON_NOT_FOUND_FORMAT = "Pokemon not found with name: %s";
        public static final String TRANSLATION_UNAVAILABLE = "Could not apply translation. Using standard description instead.";
        public static final String EXTERNAL_API_ERROR_FORMAT = "External API error: %s %s";
        public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
        public static final String NO_DESCRIPTION_AVAILABLE = "No description available";
        public static final String TRANSLATION_SERVICE_UNAVAILABLE = "Translation service unavailable";
        public static final String ERROR = "error";
        public static final String BAD_REQUEST = "Bad request";
        public static final String CONNECTION_TIMEOUT = "Connection timeout";
        public static final String DESCRIPTION_MISMATCH_ERROR = "The description does not match any expected values";
    }

    public static final class ErrorResponses {
        public static final String NOT_FOUND = "{\"error\": \"Not found\"}";
        public static final String INTERNAL_SERVER_ERROR = "{\"error\": \"Internal Server Error\"}";
        public static final String BAD_REQUEST = "{\"error\": \"Bad Request\"}";
        public static final String TOO_MANY_REQUESTS = "\"{\\\"error\\\": {\\\"code\\\": 429, \\\"message\\\": \\\"Too many requests\\\"}}\"";
    }

    public static final class TestObjects {
        public static final PokemonSpeciesDto MEWTWO_SPECIES_DTO;

        public static final TranslationResponse SHAKESPEARE_TRANSLATION_RESPONSE;
        public static final TranslationResponse YODA_TRANSLATION_RESPONSE;

        static {
            FlavorTextEntry.Language english = new FlavorTextEntry.Language();
            english.setName(Languages.ENGLISH);

            FlavorTextEntry flavorTextEntry = new FlavorTextEntry();
            flavorTextEntry.setFlavorText(Descriptions.PIKACHU_STANDARD);
            flavorTextEntry.setLanguage(english);

            PokemonSpeciesDto.Habitat habitat = new PokemonSpeciesDto.Habitat();
            habitat.setName(Habitats.RARE);

            MEWTWO_SPECIES_DTO = new PokemonSpeciesDto();
            MEWTWO_SPECIES_DTO.setName(Names.MEWTWO);
            MEWTWO_SPECIES_DTO.setFlavorTextEntries(List.of(flavorTextEntry));
            MEWTWO_SPECIES_DTO.setHabitat(habitat);
            MEWTWO_SPECIES_DTO.setLegendary(Legendary.TRUE);

            TranslationResponse.Success success = new TranslationResponse.Success();
            success.setTotal(1);

            TranslationResponse.Contents shakespeareContents = new TranslationResponse.Contents();
            shakespeareContents.setTranslated(Descriptions.PIKACHU_SHAKESPEARE);
            shakespeareContents.setText(Descriptions.PIKACHU_STANDARD);
            shakespeareContents.setTranslation(Translations.SHAKESPEARE);

            SHAKESPEARE_TRANSLATION_RESPONSE = new TranslationResponse();
            SHAKESPEARE_TRANSLATION_RESPONSE.setSuccess(success);
            SHAKESPEARE_TRANSLATION_RESPONSE.setContents(shakespeareContents);

            TranslationResponse.Contents yodaContents = new TranslationResponse.Contents();
            yodaContents.setTranslated(Descriptions.ZUBAT_YODA);
            yodaContents.setText(Descriptions.ZUBAT_STANDARD);
            yodaContents.setTranslation(Translations.YODA);

            YODA_TRANSLATION_RESPONSE = new TranslationResponse();
            YODA_TRANSLATION_RESPONSE.setSuccess(success);
            YODA_TRANSLATION_RESPONSE.setContents(yodaContents);
        }
    }
}