package ru.kpfu.itis.paramonov.dto.responses;

public record ErrorResponseDto(
        int code,
        String message) {
}
