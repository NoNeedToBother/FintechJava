package ru.kpfu.itis.paramonov.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.dto.EventEntityDto;
import ru.kpfu.itis.paramonov.entity.Event;

@Component
@AllArgsConstructor
public class EventEntityToEventEntityDtoMapper {

    private PlaceEntityToPlaceDtoMapper placeEntityToPlaceDtoMapper;

    public EventEntityDto mapFromEntity(Event event) {
        return new EventEntityDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getPrice(),
                placeEntityToPlaceDtoMapper.mapFromEntity(event.getPlace()),
                event.getDate().toLocalDateTime()
        );
    }
}
