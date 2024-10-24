package ru.kpfu.itis.paramonov.mappers;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.dto.PlaceDto;
import ru.kpfu.itis.paramonov.entity.Place;

@Component
public class PlaceEntityToPlaceDtoMapper {

    public PlaceDto mapFromEntity(Place place) {
        return new PlaceDto(
                place.getId(), place.getSlug(), place.getName()
        );
    }
}
