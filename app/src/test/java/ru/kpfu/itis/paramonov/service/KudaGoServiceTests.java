package ru.kpfu.itis.paramonov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.kpfu.itis.paramonov.config.KudaGoApiConfigurationProperties;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.dto.kudago.KudaGoCategoryResponseDto;
import ru.kpfu.itis.paramonov.dto.kudago.KudaGoCityResponseDto;
import ru.kpfu.itis.paramonov.service.impl.KudaGoApiServiceImpl;

import java.util.Collection;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KudaGoServiceTests {

    private KudaGoApiService kudaGoApiService;

    @Mock
    private WebClient webClient;

    @Mock
    private KudaGoApiConfigurationProperties kudaGoApiConfigurationProperties;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        kudaGoApiService = new KudaGoApiServiceImpl(webClient, kudaGoApiConfigurationProperties);
    }

    @Test
    public void testGetAllCities() {
        //Arrange
        KudaGoCityResponseDto cityResponseDto1 = new KudaGoCityResponseDto("a", "A");
        KudaGoCityResponseDto cityResponseDto2 = new KudaGoCityResponseDto("b", "B");
        Flux<KudaGoCityResponseDto> citiesResponseFlux = Flux.just(cityResponseDto1, cityResponseDto2);

        when(kudaGoApiConfigurationProperties.getCitiesUri()).thenReturn("https://kudago.com/public-api/v1.4/locations/");
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("https://kudago.com/public-api/v1.4/locations/")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(KudaGoCityResponseDto.class)).thenReturn(citiesResponseFlux);

        //Act
        Mono<Collection<CityDto>> resultMono = kudaGoApiService.getAllCities();

        //Assert
        StepVerifier.create(resultMono)
                .expectNextMatches(cities -> {
                    CityDto city1 = new CityDto("a", "A");
                    CityDto city2 = new CityDto("b", "B");
                    return cities.contains(city1) && cities.contains(city2);
                })
                .verifyComplete();
    }

    @Test
    public void testGetAllCategories() {
        //Arrange
        KudaGoCategoryResponseDto categoryResponseDto1 = new KudaGoCategoryResponseDto(1, "a", "A");
        KudaGoCategoryResponseDto categoryResponseDto2 = new KudaGoCategoryResponseDto(2, "b", "B");
        Flux<KudaGoCategoryResponseDto> categoriesResponseFlux = Flux.just(categoryResponseDto1, categoryResponseDto2);

        when(kudaGoApiConfigurationProperties.getCategoriesUri()).thenReturn("https://kudago.com/public-api/v1.4/place-categories/");
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("https://kudago.com/public-api/v1.4/place-categories/")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(KudaGoCategoryResponseDto.class)).thenReturn(categoriesResponseFlux);

        //Act
        Mono<Collection<CategoryDto>> resultMono = kudaGoApiService.getAllCategories();

        //Assert
        StepVerifier.create(resultMono)
                .expectNextMatches(categories -> {
                    CategoryDto category1 = new CategoryDto(1, "a", "A");
                    CategoryDto category2 = new CategoryDto(2, "b", "B");
                    return categories.contains(category1) && categories.contains(category2);
                })
                .verifyComplete();
    }
}
