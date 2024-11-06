package ru.kpfu.itis.paramonov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;
import ru.kpfu.itis.paramonov.entity.Role;
import ru.kpfu.itis.paramonov.entity.User;
import ru.kpfu.itis.paramonov.repository.UserRepository;
import ru.kpfu.itis.paramonov.service.AuthService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    @Override
    public void register(RegisterUserRequestDto registerUserRequestDto) {
        User user = User.builder()
                .username(registerUserRequestDto.getUsername())
                .password(
                        bCryptPasswordEncoder.encode(registerUserRequestDto.getPassword())
                )
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);
    }
}
