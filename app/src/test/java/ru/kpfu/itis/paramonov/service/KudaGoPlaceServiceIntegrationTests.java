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
import ru.kpfu.itis.paramonov.dto.CityDto;

import java.util.Collection;

@Testcontainers
@SpringBootTest
public class KudaGoPlaceServiceIntegrationTests {

    @Autowired
    private KudaGoApiService kudaGoApiService;

    @Container
    static WireMockContainer wireMockContainer = new WireMockContainer("wiremock/wiremock:3.6.0")
            .withMappingFromResource("cities", "city-mapping.json");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.cities-url", () -> wireMockContainer.getBaseUrl() + "/cities");
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
        Mono<Collection<CityDto>> result = kudaGoApiService.getAllCities();
        StepVerifier.create(result)
                .expectNextMatches(cities -> {
                    CityDto city1 = new CityDto("a", "A");
                    CityDto city2 = new CityDto("b", "B");
                    return cities.contains(city1) && cities.contains(city2);
                })
                .verifyComplete();
    }
}
