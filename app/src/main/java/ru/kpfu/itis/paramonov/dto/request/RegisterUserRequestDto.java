package ru.kpfu.itis.paramonov.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import ru.kpfu.itis.paramonov.validation.Password;

@Getter
@AllArgsConstructor
public class RegisterUserRequestDto {

    @NotNull
    @Length(max = 32)
    private String username;

    @NotNull
    @Password
    private String password;

}
