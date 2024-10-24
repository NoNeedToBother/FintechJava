package ru.kpfu.itis.paramonov.dto.request;

public record UpdateEventRequestDto(
        String name,
        String description,
        Double price,
        Long placeId,
        String date
) { }
