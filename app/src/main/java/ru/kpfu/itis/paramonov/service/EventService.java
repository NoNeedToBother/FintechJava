package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.EventEntityDto;
import ru.kpfu.itis.paramonov.dto.request.CreateEventRequestDto;
import ru.kpfu.itis.paramonov.dto.request.UpdateEventRequestDto;

public interface EventService {

    EventEntityDto read(Long id);

    EventEntityDto create(CreateEventRequestDto createEventRequestDto);

    EventEntityDto update(Long id, UpdateEventRequestDto updateEventRequestDto);

    EventEntityDto delete(Long id);
}
