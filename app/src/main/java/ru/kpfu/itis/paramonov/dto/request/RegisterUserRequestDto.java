package ru.kpfu.itis.paramonov.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class RegisterUserRequestDto {

    @NotNull
    @Length(max = 32)
    private String username;

    @NotNull
    private String password;

}
