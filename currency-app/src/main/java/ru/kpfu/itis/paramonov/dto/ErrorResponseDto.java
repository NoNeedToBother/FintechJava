package ru.kpfu.itis.paramonov.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponseDto(
        @Schema(example = "400")
        int code,
        @Schema(example = "Error")
        String message) {
}
