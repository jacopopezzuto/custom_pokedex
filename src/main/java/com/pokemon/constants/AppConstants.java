package com.pokemon.constants;

public final class AppConstants {

    private AppConstants() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    public static final class Cache {
        public static final String POKEMON_CACHE = "pokemonCache";
        public static final String TRANSLATION_CACHE = "translationCache";

        private Cache() {
            throw new AssertionError("Cache utility class should not be instantiated");
        }
    }

    public static final class Api {
        public static final String SHAKESPEARE_TRANSLATION = "shakespeare";
        public static final String YODA_TRANSLATION = "yoda";
        public static final String CAVE_HABITAT = "cave";

        private Api() {
            throw new AssertionError("Api utility class should not be instantiated");
        }
    }

    public static final class WebClient {
        public static final int DEFAULT_TIMEOUT_MS = 5000;
        public static final int DEFAULT_MAX_MEMORY_SIZE = 16 * 1024 * 1024;

        private WebClient() {
            throw new AssertionError("WebClient utility class should not be instantiated");
        }
    }

    public static final class Language {
        public static final String ENGLISH = "en";

        private Language() {
            throw new AssertionError("Language utility class should not be instantiated");
        }
    }
}