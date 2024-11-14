package ru.kpfu.itis.paramonov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.dto.request.LoginRequestDto;
import ru.kpfu.itis.paramonov.dto.request.PasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;
import ru.kpfu.itis.paramonov.dto.request.ValidatePasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.LoginResponseDto;
import ru.kpfu.itis.paramonov.jwt.JwtAuthentication;
import ru.kpfu.itis.paramonov.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserRequestDto registerUserRequestDto) {
        authService.register(registerUserRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        var response = authService.login(loginRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(JwtAuthentication authentication) {
        authService.logout(authentication.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid PasswordChangeRequestDto passwordChangeRequestDto,
            JwtAuthentication authentication
    ) {
        authService.handleChangePasswordRequest(
                authentication.getId(), passwordChangeRequestDto
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change/validate")
    public ResponseEntity<?> validateChangePassword(
            @RequestBody @Valid ValidatePasswordChangeRequestDto validatePasswordChangeRequestDto,
            JwtAuthentication authentication
    ) {
        authService.validateChangePasswordRequest(
                authentication.getId(), validatePasswordChangeRequestDto
        );
        return ResponseEntity.ok().build();
    }
}
