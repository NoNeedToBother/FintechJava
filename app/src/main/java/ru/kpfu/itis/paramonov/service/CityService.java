package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.CityDto;

import java.util.Collection;

public interface CityService {

    CityDto get(String slug);

    Collection<CityDto> getAll();

    boolean add(String slug, CityDto cityDto);

    CityDto remove(String slug);

    CityDto update(String slug, CityDto cityDto);

}
