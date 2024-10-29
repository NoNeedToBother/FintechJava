package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.PlaceDto;
import ru.kpfu.itis.paramonov.entity.Place;
import ru.kpfu.itis.paramonov.exception.PlaceNotFoundException;
import ru.kpfu.itis.paramonov.mappers.PlaceEntityToPlaceDtoMapper;
import ru.kpfu.itis.paramonov.repository.PlaceRepository;
import ru.kpfu.itis.paramonov.service.PlaceService;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository placeRepository;

    private PlaceEntityToPlaceDtoMapper placeEntityToPlaceDtoMapper;

    @Override
    public PlaceDto get(Long id) {
        return placeEntityToPlaceDtoMapper.mapFromEntity(
                placeRepository.findById(id)
                        .orElseThrow(() -> new PlaceNotFoundException("Place not found"))
        );
    }

    @Override
    public Collection<PlaceDto> getAll() {
        return placeRepository.findAll()
                .stream().map(place -> placeEntityToPlaceDtoMapper.mapFromEntity(place))
                .toList();
    }

    @Override
    public PlaceDto add(String slug, String name) {
        Place place = Place.builder()
                .name(name)
                .slug(slug)
                .events(new ArrayList<>())
                .build();
        return placeEntityToPlaceDtoMapper.mapFromEntity(
                placeRepository.save(place)
        );
    }

    @Override
    public PlaceDto remove(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found"));
        PlaceDto result = placeEntityToPlaceDtoMapper.mapFromEntity(place);
        placeRepository.delete(place);
        return result;
    }

    @Override
    public PlaceDto update(Long id, String slug, String name) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found"));
        if (slug != null) {
            place.setSlug(slug);
        }
        if (name != null) {
            place.setName(name);
        }
        return placeEntityToPlaceDtoMapper.mapFromEntity(
                placeRepository.save(place)
        );
    }
}
