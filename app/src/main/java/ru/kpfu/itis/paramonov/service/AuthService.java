package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.request.LoginRequestDto;
import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.LoginResponseDto;

public interface AuthService {

    void register(RegisterUserRequestDto registerUserRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
