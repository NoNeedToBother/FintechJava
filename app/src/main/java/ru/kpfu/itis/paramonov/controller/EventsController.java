package ru.kpfu.itis.paramonov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.dto.request.GetEventsRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.EventsResponseDto;
import ru.kpfu.itis.paramonov.service.EventsService;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventsService;

    @GetMapping
    public Mono<ResponseEntity<EventsResponseDto>> getEvents(@Valid GetEventsRequestDto getEventsRequestDto) {
        return eventsService.getEventsWithMono(getEventsRequestDto.getBudget(), getEventsRequestDto.getCurrency(),
                getEventsRequestDto.getDateFrom(), getEventsRequestDto.getDateTo())
                .map(events -> new ResponseEntity<>(
                        new EventsResponseDto(events), HttpStatus.OK
                ));
    }
}
