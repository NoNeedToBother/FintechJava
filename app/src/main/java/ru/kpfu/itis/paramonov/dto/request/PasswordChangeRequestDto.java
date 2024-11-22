package ru.kpfu.itis.paramonov.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.kpfu.itis.paramonov.validation.Password;

@Getter
@AllArgsConstructor
public class PasswordChangeRequestDto {

    @NotNull
    @Password
    private String newPassword;

    @NotNull
    private String previousPassword;
}
