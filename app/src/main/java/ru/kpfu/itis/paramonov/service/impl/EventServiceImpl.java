package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.EventEntityDto;
import ru.kpfu.itis.paramonov.dto.request.CreateEventRequestDto;
import ru.kpfu.itis.paramonov.dto.request.UpdateEventRequestDto;
import ru.kpfu.itis.paramonov.entity.Event;
import ru.kpfu.itis.paramonov.entity.Place;
import ru.kpfu.itis.paramonov.exception.EventNotFoundException;
import ru.kpfu.itis.paramonov.exception.PlaceNotExistsException;
import ru.kpfu.itis.paramonov.mappers.EventEntityToEventEntityDtoMapper;
import ru.kpfu.itis.paramonov.repository.EventRepository;
import ru.kpfu.itis.paramonov.repository.PlaceRepository;
import ru.kpfu.itis.paramonov.service.EventService;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private EventEntityToEventEntityDtoMapper eventEntityToEventEntityDtoMapper;

    private EventRepository eventRepository;

    private PlaceRepository placeRepository;

    @Override
    public EventEntityDto read(Long id) {
        Optional<Event> optionalEventEntityDto = eventRepository.findById(id);
        return eventEntityToEventEntityDtoMapper.mapFromEntity(
                optionalEventEntityDto.orElseThrow(() -> new EventNotFoundException("Event was not found"))
        );
    }

    @Override
    public EventEntityDto create(CreateEventRequestDto createEventRequestDto) {
        Event event = Event.builder()
                .id(null)
                .name(createEventRequestDto.name())
                .description(createEventRequestDto.description())
                .price(createEventRequestDto.price())
                .place(
                        placeRepository.findById(createEventRequestDto.placeId())
                                .orElseThrow(() -> new PlaceNotExistsException("Place id does not exist"))
                )
                .date(Timestamp.valueOf(createEventRequestDto.date()))
                .build();
        return eventEntityToEventEntityDtoMapper.mapFromEntity(eventRepository.save(event));
    }

    @Override
    public EventEntityDto update(Long id, UpdateEventRequestDto updateEventRequestDto) {
        Event previous = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event was not found"));

        if (updateEventRequestDto.name() != null) {
            previous.setName(updateEventRequestDto.name());
        }
        if (updateEventRequestDto.description() != null) {
            previous.setDescription(updateEventRequestDto.description());
        }
        if (updateEventRequestDto.price() != null) {
            previous.setPrice(updateEventRequestDto.price());
        }
        if (updateEventRequestDto.placeId() != null) {
            Place place = placeRepository.findById(updateEventRequestDto.placeId())
                    .orElseThrow(() -> new PlaceNotExistsException("Place id does not exist"));
            previous.setPlace(place);
        }
        if (updateEventRequestDto.date() != null) {
            previous.setDate(Timestamp.valueOf(updateEventRequestDto.date()));
        }

        return eventEntityToEventEntityDtoMapper.mapFromEntity(
                eventRepository.save(previous)
        );
    }

    @Override
    public EventEntityDto delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event was not found"));
        EventEntityDto result = eventEntityToEventEntityDtoMapper.mapFromEntity(event);
        eventRepository.delete(event);
        return result;
    }
}
