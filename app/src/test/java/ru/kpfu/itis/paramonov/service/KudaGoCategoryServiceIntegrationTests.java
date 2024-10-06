package ru.kpfu.itis.paramonov.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.kpfu.itis.paramonov.dto.CategoryDto;

import java.util.Collection;


@Testcontainers
@SpringBootTest
public class KudaGoCategoryServiceIntegrationTests {

    @Autowired
    private KudaGoApiService kudaGoApiService;

    @Container
    static WireMockContainer wireMockContainer = new WireMockContainer("wiremock/wiremock:3.6.0")
            .withMappingFromResource("categories", "category-mapping.json");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.categories-url", () -> wireMockContainer.getBaseUrl() + "/categories");
    }

    @BeforeEach
    public void setUp() {
        wireMockContainer.start();
    }

    @AfterEach
    public void stop() {
        wireMockContainer.stop();
    }

    @Test
    public void testGetCities() {
        Mono<Collection<CategoryDto>> result = kudaGoApiService.getAllCategories();
        StepVerifier.create(result)
                .expectNextMatches(categories -> {
                    CategoryDto category1 = new CategoryDto(1, "a", "A");
                    CategoryDto category2 = new CategoryDto(2, "b", "B");
                    return categories.contains(category1) && categories.contains(category2);
                })
                .verifyComplete();
    }
}

