package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.CityDto;

import java.util.Collection;

public interface CityService {

    CityDto get(String slug);

    Collection<CityDto> getAll();

    CityDto add(String slug, String name);

    CityDto remove(String slug);

    CityDto update(String slug, String name);
}
