package ru.kpfu.itis.paramonov.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.kpfu.itis.paramonov.dto.CityDto;

import java.util.Collection;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@Testcontainers
@SpringBootTest
public class KudaGoCityServiceIntegrationTests {

    @Autowired
    private KudaGoApiService kudaGoApiService;

    @Container
    static WireMockContainer wireMockContainer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.cities-url", wireMockContainer::getBaseUrl);
    }

    @BeforeEach
    public void setUp() {
        wireMockContainer.start();

        WireMock.get(WireMock.urlMatching(wireMockContainer.getBaseUrl()))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        """
                                                [
                                                  {
                                                    "slug":"a",
                                                    "name":"A"
                                                  },
                                                  {
                                                    "slug":"b",
                                                    "name":"B"
                                                  }
                                                ]
                                        """
                                )
                );

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
