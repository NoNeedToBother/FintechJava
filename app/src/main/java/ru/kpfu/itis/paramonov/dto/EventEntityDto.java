package ru.kpfu.itis.paramonov.dto;

import java.time.LocalDateTime;

public record EventEntityDto(
        Long id,
        String name,
        String description,
        Double price,
        CityDto place,
        LocalDateTime date
) { }
