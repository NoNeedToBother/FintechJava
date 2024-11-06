package ru.kpfu.itis.paramonov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.request.LoginRequestDto;
import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.LoginResponseDto;
import ru.kpfu.itis.paramonov.entity.Role;
import ru.kpfu.itis.paramonov.entity.User;
import ru.kpfu.itis.paramonov.exception.InvalidCredentialsException;
import ru.kpfu.itis.paramonov.jwt.JwtProvider;
import ru.kpfu.itis.paramonov.repository.UserRepository;
import ru.kpfu.itis.paramonov.service.AuthService;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterUserRequestDto registerUserRequestDto) {
        Optional<User> potentialUser = userRepository.findByUsername(registerUserRequestDto.getUsername());
        if (potentialUser.isPresent()) {
            throw new InvalidCredentialsException("User with this username already exists");
        }
        User user = User.builder()
                .username(registerUserRequestDto.getUsername())
                .password(
                        bCryptPasswordEncoder.encode(registerUserRequestDto.getPassword())
                )
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<User> user = userRepository.findByUsername(loginRequestDto.getUsername());
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Incorrect credentials");
        }
        boolean rememberMe = false;
        if (loginRequestDto.getRemember() != null) {
            rememberMe = loginRequestDto.getRemember();
        }

        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) {
            String token = jwtProvider.generateToken(user.get(), rememberMe);
            return new LoginResponseDto(token);
        } else {
            throw new InvalidCredentialsException("Incorrect credentials");
        }

    }
}
