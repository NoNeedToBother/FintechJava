package ru.kpfu.itis.paramonov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.config.KudaGoApiConfigurationProperties;
import ru.kpfu.itis.paramonov.dto.EventDto;
import ru.kpfu.itis.paramonov.dto.kudago.events.KudaGoEventResponseDto;
import ru.kpfu.itis.paramonov.dto.kudago.events.KudaGoEventsResponseDto;
import ru.kpfu.itis.paramonov.service.CurrencyService;
import ru.kpfu.itis.paramonov.service.EventService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final WebClient webClient;

    private final KudaGoApiConfigurationProperties kudaGoConfig;

    private final CurrencyService currencyService;

    private final int MAX_EVENT_PAGE = 1000;

    @Override
    public Mono<List<EventDto>> getEventsWithMono(Double budget, String currencyFrom, String dateFrom, String dateTo) {
        var currencyMono = currencyService.convertCurrenciesWithMono(currencyFrom, budget);

        var eventsMono = Flux.range(1, MAX_EVENT_PAGE)
                .concatMap(page -> getAllEvents(page, dateFrom, dateTo))
                .takeWhile(events -> !events.getEvents().isEmpty())
                .flatMapIterable(KudaGoEventsResponseDto::getEvents)
                .collectList()
                .map(KudaGoEventsResponseDto::new);

        return Mono.zip(currencyMono, eventsMono)
                .map(tuple -> {
                    var currencyDto = tuple.getT1();
                    var events = tuple.getT2();
                    return events.getEvents()
                            .stream()
                            .flatMap(event -> mapEventResponseToDto(event).stream())
                            .filter(event -> currencyDto.convertedAmount() > event.price())
                            .toList();
                });
    }

    @Override
    public CompletableFuture<List<EventDto>> getEventsWithCompletableFuture(Double budget, String currencyFrom, String dateFrom, String dateTo) {
        return null;
    }

    private Mono<KudaGoEventsResponseDto> getAllEvents(int page, String dateFrom, String dateTo) {
        var eventParameters = List.of(
                "id", "dates", "title", "description", "price", "is_free"
        );

        return webClient.get()
                .uri(UriComponentsBuilder.fromUriString(kudaGoConfig.getEventsUri())
                        .queryParam("fields", String.join(",", eventParameters))
                        .queryParam("text_format", "plain")
                        .queryParam("page_size", 100)
                        .queryParam("page", page)
                        .toUriString())
                .retrieve()
                .bodyToMono(KudaGoEventsResponseDto.class);
    }

    private Optional<EventDto> mapEventResponseToDto(KudaGoEventResponseDto kudaGoEventResponseDto) {
        var price = 0.0;
        if (!kudaGoEventResponseDto.getIsFree()) {
            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
            Matcher matcher = pattern.matcher(kudaGoEventResponseDto.getPrice());
            if (matcher.find()) {
                String result = matcher.group();
                price = Double.parseDouble(result);
            } else {
                return Optional.empty();
            }
        }
        var dates = kudaGoEventResponseDto.getDates()
                .stream().map(date -> new EventDto.EventDateDto(
                        convertEpochSecondsToLocalDateTime(date.getStart()),
                        convertEpochSecondsToLocalDateTime(date.getEnd())
                ))
                .toList();
        return Optional.of(new EventDto(
                kudaGoEventResponseDto.getId(),
                kudaGoEventResponseDto.getTitle(),
                kudaGoEventResponseDto.getDescription(),
                price,
                dates
        ));
    }

    private LocalDateTime convertEpochSecondsToLocalDateTime(Long epochSeconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3)));
    }
}
