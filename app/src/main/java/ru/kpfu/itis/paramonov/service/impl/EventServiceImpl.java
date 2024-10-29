package ru.kpfu.itis.paramonov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.kpfu.itis.paramonov.config.KudaGoApiConfigurationProperties;
import ru.kpfu.itis.paramonov.dto.EventDto;
import ru.kpfu.itis.paramonov.dto.kudago.events.KudaGoEventResponseDto;
import ru.kpfu.itis.paramonov.dto.kudago.events.KudaGoEventsResponseDto;
import ru.kpfu.itis.paramonov.exception.DateFormatException;
import ru.kpfu.itis.paramonov.exception.RemoteGatewayErrorException;
import ru.kpfu.itis.paramonov.service.CurrencyService;
import ru.kpfu.itis.paramonov.service.EventService;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final WebClient webClient;

    private final KudaGoApiConfigurationProperties kudaGoConfig;

    private final CurrencyService currencyService;

    @Qualifier("kudagoApiEventRateLimiter")
    @Autowired
    private Semaphore eventRateLimiter;

    private final int MAX_EVENT_PAGE = 10;

    @Override
    public Mono<List<EventDto>> getEventsWithMono(Double budget, String currencyFrom, String dateFrom, String dateTo) {
        var currencyMono = currencyService.convertCurrenciesWithMono(currencyFrom, budget);

        var eventsMono = Flux.range(1, MAX_EVENT_PAGE)
                .concatMap(page -> getAllEvents(page, dateFrom, dateTo))
                .onErrorResume(e -> Mono.error(
                        new RemoteGatewayErrorException("Remote gateway error")
                ))
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
        var currencyFuture = currencyService.convertCurrenciesWithCompletableFuture(currencyFrom, budget);

        var eventsFuture = Flux.range(1, MAX_EVENT_PAGE)
                .concatMap(page -> getAllEvents(page, dateFrom, dateTo))
                .onErrorResume(e -> Mono.error(
                        new RemoteGatewayErrorException("Remote gateway error")
                ))
                .takeWhile(events -> !events.getEvents().isEmpty())
                .flatMapIterable(KudaGoEventsResponseDto::getEvents)
                .collectList()
                .map(KudaGoEventsResponseDto::new)
                .toFuture();

        CompletableFuture<List<EventDto>> resultFuture = new CompletableFuture<>();

        currencyFuture.thenAcceptBoth(eventsFuture, (currency, events) -> {
            var result = events.getEvents()
                    .stream()
                    .flatMap(event -> mapEventResponseToDto(event).stream())
                    .filter(event -> currency.convertedAmount() > event.price())
                    .toList();
            resultFuture.complete(result);
        });
        return resultFuture;
    }

    private Mono<KudaGoEventsResponseDto> getAllEvents(int page, String dateFrom, String dateTo) {
        try {
            eventRateLimiter.acquire();
            var eventParameters = List.of(
                    "id", "dates", "title", "description", "price", "is_free"
            );
            long actualSince;
            long actualUntil;
            Calendar calendar = Calendar.getInstance();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.UK);
            if (dateTo != null) {
                try {
                    LocalDate date = LocalDate.parse(dateTo, formatter);
                    actualUntil = date.atTime(23, 59, 59).toEpochSecond(ZoneOffset.ofHours(3));
                } catch (DateTimeParseException e) {
                    throw new DateFormatException("Unsupported date format, use dd-MM-yyyy format");
                }
            } else {
                actualUntil = calendar.getTimeInMillis() / 1000;
            }

            if (dateFrom != null) {
                try {
                    LocalDate date = LocalDate.parse(dateFrom, formatter);
                    actualSince = date.atStartOfDay().toEpochSecond(ZoneOffset.ofHours(3));
                } catch (DateTimeParseException e) {
                    throw new DateFormatException("Unsupported date format, use dd-MM-yyyy format");
                }
            } else {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
                actualSince = calendar.getTimeInMillis() / 1000;
            }

            return webClient.get()
                    .uri(UriComponentsBuilder.fromUriString(kudaGoConfig.getEventsUri())
                            .queryParam("fields", String.join(",", eventParameters))
                            .queryParam("text_format", "plain")
                            .queryParam("page_size", 20)
                            .queryParam("actual_since", actualSince)
                            .queryParam("actual_until", actualUntil)
                            .queryParam("page", page)
                            .toUriString())
                    .retrieve()
                    .bodyToMono(KudaGoEventsResponseDto.class)
                    .onErrorResume(e -> Mono.error(
                            new RemoteGatewayErrorException("Remote gateway error")
                    ));
        } catch (InterruptedException e) {
            log.warn("Event data thread got interrupted");
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            eventRateLimiter.release();
        }
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
