package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;

public interface AuthService {

    void register(RegisterUserRequestDto registerUserRequestDto);
}
