package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.PlaceDto;

import java.util.Collection;

public interface PlaceService {

    PlaceDto get(Long id);

    Collection<PlaceDto> getAll();

    PlaceDto add(String slug, String name);

    PlaceDto remove(Long id);

    PlaceDto update(Long id, String slug, String name);

}
