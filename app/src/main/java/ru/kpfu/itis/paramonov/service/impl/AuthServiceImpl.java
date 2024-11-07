package ru.kpfu.itis.paramonov.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.dto.request.PasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.dto.request.LoginRequestDto;
import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;
import ru.kpfu.itis.paramonov.dto.request.ValidatePasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.LoginResponseDto;
import ru.kpfu.itis.paramonov.entity.PasswordChangeRequest;
import ru.kpfu.itis.paramonov.entity.Role;
import ru.kpfu.itis.paramonov.entity.Token;
import ru.kpfu.itis.paramonov.entity.User;
import ru.kpfu.itis.paramonov.exception.InvalidCodeException;
import ru.kpfu.itis.paramonov.exception.InvalidCredentialsException;
import ru.kpfu.itis.paramonov.jwt.JwtProvider;
import ru.kpfu.itis.paramonov.repository.PasswordChangeRequestRepository;
import ru.kpfu.itis.paramonov.repository.TokenRepository;
import ru.kpfu.itis.paramonov.repository.UserRepository;
import ru.kpfu.itis.paramonov.service.AuthService;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordChangeRequestRepository passwordChangeRequestRepository;

    private final JwtProvider jwtProvider;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
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
    @Transactional
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
            Token dbToken = Token.builder()
                    .token(token)
                    .invalidated(false)
                    .user(user.get())
                    .build();
            tokenRepository.save(dbToken);
            return new LoginResponseDto(token);
        } else {
            throw new InvalidCredentialsException("Incorrect credentials");
        }
    }

    @Override
    @Transactional
    public void logout(Long id) {
        tokenRepository.invalidateAllTokensByUserId(id);
    }

    @Override
    @Transactional
    public void handleChangePasswordRequest(Long id, PasswordChangeRequestDto passwordChangeRequestDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Incorrect user id");
        }

        if (passwordEncoder.matches(passwordChangeRequestDto.getPreviousPassword(), user.get().getPassword())) {
            passwordChangeRequestRepository.removePasswordChangeRequestsByUser(user.get().getId());
            PasswordChangeRequest request = PasswordChangeRequest.builder()
                    .user(user.get())
                    .code("0000")
                    .newPassword(passwordEncoder.encode(
                            passwordChangeRequestDto.getNewPassword()
                    ))
                    .previousPassword(passwordEncoder.encode(
                            passwordChangeRequestDto.getPreviousPassword()
                    ))
                    .build();

            // code sending logic should be here

            passwordChangeRequestRepository.save(request);
        } else {
            throw new InvalidCredentialsException("Incorrect credentials");
        }
    }

    @Override
    @Transactional
    public void validateChangePasswordRequest(Long id, ValidatePasswordChangeRequestDto validatePasswordChangeRequestDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Incorrect user id");
        }

        PasswordChangeRequest passwordChangeRequest = passwordChangeRequestRepository.getRequestByUserId(user.get().getId());
        if (passwordChangeRequest.getCode().equals(validatePasswordChangeRequestDto.getCode())) {
            userRepository.updatePassword(
                    user.get().getId(),
                    passwordChangeRequest.getNewPassword()
            );
            passwordChangeRequestRepository.removePasswordChangeRequestsByUser(user.get().getId());
        } else {
            throw new InvalidCodeException("Invalid code");
        }
    }

}
