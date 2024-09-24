package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.dto.kudago.KudaGoCategoryResponseDto;
import ru.kpfu.itis.paramonov.dto.kudago.KudaGoCityResponseDto;
import ru.kpfu.itis.paramonov.service.KudaGoApiService;

import java.util.Collection;

@AllArgsConstructor
@Service
public class KudaGoApiServiceImpl implements KudaGoApiService {

    private WebClient webClient;

    private final String KUDA_GO_CATEGORIES_URl = "https://kudago.com/public-api/v1.4/place-categories/";

    private final String KUDA_GO_CITIES_URl = "https://kudago.com/public-api/v1.4/locations/";

    @Override
    public Mono<Collection<CityDto>> getAllCities() {
        return webClient.get()
                .uri(KUDA_GO_CITIES_URl)
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
                .uri(KUDA_GO_CATEGORIES_URl)
                .retrieve()
                .bodyToFlux(KudaGoCategoryResponseDto.class)
                .collectList()
                .map(response -> response.stream()
                        .map(dto -> new CategoryDto(dto.getId(), dto.getSlug(), dto.getName()))
                        .toList());
    }
}
