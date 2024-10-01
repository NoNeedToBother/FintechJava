package ru.kpfu.itis.paramonov.service;

import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;

import java.util.Collection;

public interface KudaGoApiService {

    Mono<Collection<CityDto>> getAllCities();

    Mono<Collection<CategoryDto>> getAllCategories();
}
