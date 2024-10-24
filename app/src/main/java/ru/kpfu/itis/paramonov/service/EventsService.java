package ru.kpfu.itis.paramonov.service;

import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.dto.EventDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventsService {

    Mono<List<EventDto>> getEventsWithMono(Double budget, String currencyFrom, String dateFrom, String dateTo);

    CompletableFuture<List<EventDto>> getEventsWithCompletableFuture(Double budget, String currencyFrom, String dateFrom, String dateTo);
}
