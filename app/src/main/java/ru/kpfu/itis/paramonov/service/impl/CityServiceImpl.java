package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.entity.Place;
import ru.kpfu.itis.paramonov.exception.PlaceNotFoundException;
import ru.kpfu.itis.paramonov.mappers.PlaceEntityToCityDtoMapper;
import ru.kpfu.itis.paramonov.repository.PlaceRepository;
import ru.kpfu.itis.paramonov.service.CityService;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private PlaceRepository placeRepository;

    private PlaceEntityToCityDtoMapper placeEntityToCityDtoMapper;

    @Override
    public CityDto get(Long id) {
        return placeEntityToCityDtoMapper.mapFromEntity(
                placeRepository.findById(id)
                        .orElseThrow(() -> new PlaceNotFoundException("Place not found"))
        );
    }

    @Override
    public Collection<CityDto> getAll() {
        return placeRepository.findAll()
                .stream().map(place -> placeEntityToCityDtoMapper.mapFromEntity(place))
                .toList();
    }

    @Override
    public CityDto add(String slug, String name) {
        Place place = Place.builder()
                .name(name)
                .slug(slug)
                .events(new ArrayList<>())
                .build();
        return placeEntityToCityDtoMapper.mapFromEntity(
                placeRepository.save(place)
        );
    }

    @Override
    public CityDto remove(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found"));
        CityDto result = placeEntityToCityDtoMapper.mapFromEntity(place);
        placeRepository.delete(place);
        return result;
    }

    @Override
    public CityDto update(Long id, String slug, String name) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found"));
        if (slug != null) {
            place.setSlug(slug);
        }
        if (name != null) {
            place.setName(name);
        }
        return placeEntityToCityDtoMapper.mapFromEntity(
                placeRepository.save(place)
        );
    }
}
