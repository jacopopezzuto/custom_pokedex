# Pokedex REST API

This project implements a RESTful API for a fun Pokedex that fetches Pokemon information from the [PokeAPI](https://pokeapi.co/) and translates descriptions using the [Fun Translations API](https://funtranslations.com/).

## Features

- **Basic Pokemon Information Endpoint**: Retrieve standard Pokemon description and additional information
- **Translated Pokemon Description Endpoint**: Retrieve Pokemon info with "fun" translation of the description
    - Yoda translation for legendary Pokemons or those whose habitat is cave
    - Shakespeare translation for all other Pokemons
    - Fallback to standard description if translation is unavailable

## Technology Stack

- Java 17
- Spring Boot 3.2.4
- Spring WebFlux (reactive programming)
- Spring Cache with Caffeine
- JUnit 5 & Mockito for testing
- Maven
- Docker

## Dependencies and Versions

The application uses the following main dependencies:

| Dependency | Version | Description |
|------------|---------|-------------|
| Spring Boot | 3.2.4 | Framework for building Java applications |
| Spring WebFlux | - | Reactive web framework |
| Spring Cache | - | Caching abstraction |
| Spring Validation | - | Data validation |
| Spring Actuator | - | Application monitoring and management |
| Caffeine | - | High performance, in-memory caching library |
| Lombok | - | Java annotation library for reducing boilerplate code |
| JUnit 5 | - | Testing framework |
| Mockito | - | Mocking framework for testing |
| Reactor Test | - | Testing utilities for reactive applications |
| OkHttp3 MockWebServer | 4.12.0 | Web server for testing HTTP clients |

All Spring Boot dependencies follow the parent version (3.2.4).

## API Endpoints

### 1. Basic Pokemon Information

```
GET /pokemon/{name}
```

Example response:
```json
{
  "name": "mewtwo",
  "description": "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
  "habitat": "rare",
  "legendary": true
}
```

### 2. Translated Pokemon Description

```
GET /pokemon/translated/{name}
```

Example response:
```json
{
  "name": "mewtwo",
  "description": "Created by a scientist after years of horrific gene splicing and dna engineering experiments, it was.",
  "habitat": "rare",
  "legendary": true
}
```

## How to Run the Application

### Prerequisites

- Java 17 or higher
- Maven
- Docker (optional)

### Using Maven

1. Clone the repository
```bash
git clone <repository-url>
cd custom_pokedex
```

2. Build the application
```bash
# Build without running tests
mvn clean package -DskipTests

# Build with tests
mvn clean package
```

3. Run the application
```bash
java -jar target/custom_pokedex-1.0-SNAPSHOT.jar
```

The API will be available at: http://localhost:5000

### Using Docker (Recommended)

Docker allows you to run the application without installing Java or Maven locally:

1. Clone the repository
```bash
git clone <repository-url>
cd custom_pokedex
```

2. Build the Docker image
```bash
docker build -t pokemon-api .
```

3. Run the container
```bash
docker run -p 5000:5000 pokemon-api
```

The API will be available at: http://localhost:5000

### Verifying the Application is Running

Once the application is running, you can check its health with:
```bash
curl http://localhost:5000/actuator/health
```

Expected response:
```json
{"status":"UP"}
```

### Example API Usage

1. Get basic Pokemon information:
```bash
curl http://localhost:5000/pokemon/pikachu
```

2. Get Pokemon information with translated description:
```bash
curl http://localhost:5000/pokemon/translated/mewtwo
```

## Production Considerations - What I Would Do Differently

If this were to be deployed in a production environment, I would consider the following improvements:

### Code Improvements

1. **Java Records for DTOs**: Replace traditional DTOs with Java Records to reduce boilerplate code and improve immutability:
   ```java
   public record PokemonResponse(String name, String description, String habitat, boolean isLegendary) {}
   ```

2. **Strategy Pattern for Translations**: Implement the Strategy pattern to make translation selection more flexible and extensible:
   ```java
   public interface TranslationStrategy {
       Mono<String> translate(String text);
   }
   
   public class YodaTranslationStrategy implements TranslationStrategy {
       private final TranslationApiClient client;
       
       @Override
       public Mono<String> translate(String text) {
           return client.translateToYoda(text);
       }
   }
   
   public class ShakespeareTranslationStrategy implements TranslationStrategy {
       private final TranslationApiClient client;
       
       @Override
       public Mono<String> translate(String text) {
           return client.translateToShakespeare(text);
       }
   }
   
   // Then in the service:
   TranslationStrategy strategy = pokemon.isLegendary() || "cave".equals(pokemon.getHabitat()) 
       ? yodaStrategy : shakespeareStrategy;
   return strategy.translate(description);
   ```

3. **API Versioning**: Add versioning to API endpoints for better backwards compatibility:
   ```
   /api/v1/pokemon/{name}
   /api/v1/pokemon/translated/{name}
   ```

4. **Circuit Breaker Pattern**: Implement circuit breakers using Resilience4j to handle external API failures gracefully:
   ```java
   // Configuration
   @Bean
   public CircuitBreakerRegistry circuitBreakerRegistry() {
       CircuitBreakerConfig config = CircuitBreakerConfig.custom()
           .failureRateThreshold(50)
           .waitDurationInOpenState(Duration.ofMillis(1000))
           .permittedNumberOfCallsInHalfOpenState(2)
           .slidingWindowSize(10)
           .build();
       return CircuitBreakerRegistry.of(config);
   }
   
   // Usage in service
   @CircuitBreaker(name = "translationApi", fallbackMethod = "fallbackTranslation")
   public Mono<String> translateText(String text) {
       return translationApiClient.translate(text);
   }
   
   public Mono<String> fallbackTranslation(String text, Exception ex) {
       log.warn("Translation service unavailable. Using original text.", ex);
       return Mono.just(text);
   }
   ```

### Performance & Scalability

1. **Distributed Caching**: Replace in-memory Caffeine cache with Redis for better scalability across multiple instances.

2. **Reactive MongoDB**: Use a reactive database for any persistent storage needs to maintain the fully reactive pipeline.

3. **Request Tracing**: Implement distributed tracing.

4. **Rate Limiting**: Implement rate limiting to protect the API, especially for the Fun Translations API which has usage limits:
   ```java
   @Bean
   public ReactiveRateLimiter translationRateLimiter() {
       return RedisRateLimiter.builder()
               .replenishRate(10)
               .burstCapacity(20)
               .build();
   }
   ```

### DevOps Considerations

1. **Kubernetes Deployment**: Package the application for Kubernetes with proper health checks and resource limits.

2. **CI/CD Pipeline**: Set up automated testing, building, and deployment.

3. **Monitoring**: Add Prometheus metrics and Grafana dashboards for comprehensive monitoring.

4. **Secrets Management**: Use a vault service for storing API keys and other sensitive data.

### Security Improvements

1. **API Key Authentication**: Implement API key authentication for consumers of your API.

2. **HTTPS**: Enforce HTTPS for all API traffic.

3. **Input Validation & Sanitization**: Add more comprehensive input validation.

4. **Output Data Filtering**: Ensure only necessary data is returned in responses.

## Testing

The project includes unit tests, integration tests, and caching tests to ensure code quality and functionality.

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PokemonServiceTest

# Run only integration tests
mvn test -Dtest=*IntegrationTest

# Run only the tests that make real API calls (tagged with "realApi")
mvn test -Dgroups=realApi
```

## Code Coverage

The project achieves 100% test coverage across all metrics:

- 100% Class coverage
- 100% Method coverage
- 100% Line coverage

This comprehensive test coverage ensures that every part of the codebase is thoroughly tested, reducing the likelihood of undiscovered bugs and making the application more robust and maintainable.

The test suite includes:
- Unit tests for individual components
- Integration tests for API endpoints
- Caching tests to verify proper caching behavior
- Tests with mocked external dependencies
- Real API integration tests (tagged as "realApi")

To generate and view the coverage report:

```bash
mvn clean verify
```

The coverage report will be available at: `target/site/jacoco/index.html`