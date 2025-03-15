package com.pokemon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class PokedexApplicationTest {

    @Test
    void contextLoads() {
        PokedexApplication application = new PokedexApplication();
        assertThat(application).isNotNull();
    }

    @Test
    void mainMethod_shouldCallSpringApplicationRun() {
        try (var springAppMock = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext contextMock = mock(ConfigurableApplicationContext.class);
            springAppMock.when(() -> SpringApplication.run(
                    eq(PokedexApplication.class),
                    any(String[].class))
            ).thenReturn(contextMock);

            String[] args = new String[]{"arg1", "arg2"};
            PokedexApplication.main(args);

            springAppMock.verify(() -> SpringApplication.run(
                    eq(PokedexApplication.class),
                    eq(args))
            );
        }
    }
}