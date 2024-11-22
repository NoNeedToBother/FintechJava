package ru.kpfu.itis.paramonov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.dto.EventEntityDto;
import ru.kpfu.itis.paramonov.dto.request.CreateEventRequestDto;
import ru.kpfu.itis.paramonov.dto.request.UpdateEventRequestDto;
import ru.kpfu.itis.paramonov.service.EventService;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<EventEntityDto> read(@PathVariable("id") long id) {
        return new ResponseEntity<>(
                eventService.read(id), HttpStatus.OK
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventEntityDto> create(@RequestBody CreateEventRequestDto createEventRequestDto) {
        return new ResponseEntity<>(
                eventService.create(createEventRequestDto), HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventEntityDto> update(
            @PathVariable("id") long id,
            @RequestBody UpdateEventRequestDto updateEventRequestDto) {
        return new ResponseEntity<>(
                eventService.update(id, updateEventRequestDto), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventEntityDto> delete(@PathVariable("id") long id) {
        return new ResponseEntity<>(
                eventService.delete(id), HttpStatus.OK
        );
    }
}
