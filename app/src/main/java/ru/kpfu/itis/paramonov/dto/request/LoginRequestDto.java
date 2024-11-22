package ru.kpfu.itis.paramonov.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private Boolean remember;
}
