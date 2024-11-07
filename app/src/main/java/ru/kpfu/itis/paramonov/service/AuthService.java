package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.request.PasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.dto.request.LoginRequestDto;
import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;
import ru.kpfu.itis.paramonov.dto.request.ValidatePasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.LoginResponseDto;

public interface AuthService {

    void register(RegisterUserRequestDto registerUserRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    void logout(Long id);

    void handleChangePasswordRequest(Long id, PasswordChangeRequestDto passwordChangeRequestDto);

    void validateChangePasswordRequest(Long id, ValidatePasswordChangeRequestDto validatePasswordChangeRequestDto);
}
