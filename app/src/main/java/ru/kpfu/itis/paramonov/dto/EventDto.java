package ru.kpfu.itis.paramonov.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        Long id,
        String title,
        String description,
        Double price,
        List<EventDateDto> dates
) {

    public record EventDateDto(
            LocalDateTime start,
            LocalDateTime finish
    ) {}
}
