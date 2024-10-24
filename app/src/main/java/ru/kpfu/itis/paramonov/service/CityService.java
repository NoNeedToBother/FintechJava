package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.CityDto;

import java.util.Collection;

public interface CityService {

    CityDto get(Long id);

    Collection<CityDto> getAll();

    CityDto add(String slug, String name);

    CityDto remove(Long id);

    CityDto update(Long id, String slug, String name);

}
