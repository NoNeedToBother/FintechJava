package ru.kpfu.itis.paramonov.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateEventRequestDto(
        @NotNull String name,
        @NotNull String description,
        @NotNull Double price,
        @NotNull Long placeId,
        @NotNull String date
) { }
