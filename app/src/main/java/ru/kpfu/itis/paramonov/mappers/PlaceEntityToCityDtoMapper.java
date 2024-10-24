package ru.kpfu.itis.paramonov.mappers;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.entity.Place;

@Component
public class PlaceEntityToCityDtoMapper {

    public CityDto mapFromEntity(Place place) {
        return new CityDto(
                place.getSlug(), place.getName()
        );
    }
}
