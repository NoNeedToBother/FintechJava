package ru.kpfu.itis.paramonov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.config.KudaGoApiConfigurationProperties;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.dto.kudago.KudaGoCategoryResponseDto;
import ru.kpfu.itis.paramonov.dto.kudago.KudaGoCityResponseDto;
import ru.kpfu.itis.paramonov.service.KudaGoApiService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class KudaGoApiServiceImpl implements KudaGoApiService {

    private final WebClient webClient;

    private final KudaGoApiConfigurationProperties kudaGoConfig;

    @Override
    public Mono<Collection<CityDto>> getAllCities() {
        return webClient.get()
                .uri(kudaGoConfig.getCitiesUri())
                .retrieve()
                .bodyToFlux(KudaGoCityResponseDto.class)
                .collectList()
                .map(response -> response.stream()
                        .map(dto -> new CityDto(dto.getSlug(), dto.getName()))
                        .toList());
    }

    @Override
    public Mono<Collection<CategoryDto>> getAllCategories() {
        return webClient.get()
                .uri(kudaGoConfig.getCategoriesUri())
                .retrieve()
                .bodyToFlux(KudaGoCategoryResponseDto.class)
                .collectList()
                .map(response -> response.stream()
                        .map(dto -> new CategoryDto(dto.getId(), dto.getSlug(), dto.getName()))
                        .toList());
    }
}
