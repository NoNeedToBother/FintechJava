package ru.kpfu.itis.paramonov.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kpfu.itis.paramonov.dto.request.LoginRequestDto;
import ru.kpfu.itis.paramonov.dto.request.PasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.dto.request.RegisterUserRequestDto;
import ru.kpfu.itis.paramonov.dto.request.ValidatePasswordChangeRequestDto;
import ru.kpfu.itis.paramonov.entity.PasswordChangeRequest;
import ru.kpfu.itis.paramonov.entity.Token;
import ru.kpfu.itis.paramonov.entity.User;
import ru.kpfu.itis.paramonov.jwt.JwtProvider;
import ru.kpfu.itis.paramonov.repository.PasswordChangeRequestRepository;
import ru.kpfu.itis.paramonov.repository.TokenRepository;
import ru.kpfu.itis.paramonov.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AuthTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() throws Exception {
        try (Connection connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("truncate table users cascade");
                statement.execute("truncate table user_tokens cascade");
                statement.execute("truncate table password_change_requests cascade");
            }
        }
    }

    @Test
    void registerUser_successfully_test() throws Exception {
        // Arrange
        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto(
                "username", "password"
        );
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequestDto)))
                .andExpect(status().isOk());
        Optional<User> dbUser = userRepository.findByUsername("username");
        assertAll(
                () -> assertTrue(dbUser.isPresent()),
                () -> assertTrue(bCryptPasswordEncoder.matches(
                        "password", dbUser.get().getPassword()
                ))
        );
    }

    @Test
    void registerUser_missingCredentials_test() throws Exception {
        // Arrange
        String json = """
                {
                    "username":"username"
                }
                """;
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        Optional<User> dbUser = userRepository.findByUsername("username");
        assertFalse(dbUser.isPresent());
    }

    @Test
    void registerUser_usernameTaken_test() throws Exception {
        // Arrange
        String encodedPassword = bCryptPasswordEncoder.encode("password");
        User user = User.builder()
                .username("username")
                .password(encodedPassword)
                .build();
        userRepository.save(user);
        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto(
                "username", "password"
        );
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("User with this username already exists"));
    }

    @Test
    void loginUser_successfully_test() throws Exception {
        // Arrange
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "username", "password", null
        );
        String encodedPassword = bCryptPasswordEncoder.encode("password");
        User user = User.builder()
                .username("username")
                .password(encodedPassword)
                .build();
        userRepository.save(user);
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void loginUser_missingCredentials_test() throws Exception {
        // Arrange
        String json = """
                {
                    "username":"username"
                }
                """;
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUser_noUser_test() throws Exception {
        // Arrange
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "username", "password", null
        );
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Incorrect credentials"));
    }

    @Test
    void loginUser_incorrectPassword_test() throws Exception {
        // Arrange
        String encodedPassword = bCryptPasswordEncoder.encode("passwor");
        User user = User.builder()
                .username("username")
                .password(encodedPassword)
                .build();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "username", "password", null
        );
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Incorrect credentials"));
    }

    @Test
    void logout_successfully_test() throws Exception {
        //Arrange
        String encodedPassword = bCryptPasswordEncoder.encode("password");
        User user = User.builder()
                .username("username")
                .password(encodedPassword)
                .roles(new HashSet<>())
                .build();
        User savedUser = userRepository.save(user);
        String jwt = jwtProvider.generateToken(savedUser, false);
        Token token = tokenRepository.save(
                Token.builder()
                        .token(jwt)
                        .invalidated(false)
                        .user(savedUser)
                        .build()
        );
        tokenRepository.save(token);
        //Act, Assert
        mockMvc.perform(get("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
        Token dbToken = tokenRepository.getTokenByUserIdAndToken(savedUser.getId(), jwt);
        assertTrue(dbToken.isInvalidated());
        mockMvc.perform(get("/api/v1/event/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePassword_successfully_test() throws Exception {
        // Arrange
        String encodedPassword = bCryptPasswordEncoder.encode("password");
        User user = User.builder()
                .username("username")
                .password(encodedPassword)
                .roles(new HashSet<>())
                .build();
        User savedUser = userRepository.save(user);
        PasswordChangeRequestDto passwordChangeRequestDto = new PasswordChangeRequestDto(
                "passw", "password"
        );
        String jwt = jwtProvider.generateToken(savedUser, false);
        Token token = tokenRepository.save(
                Token.builder()
                        .token(jwt)
                        .invalidated(false)
                        .user(savedUser)
                        .build()
        );
        tokenRepository.save(token);
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/password/change")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void validateChangePassword_successfully_test() throws Exception {
        // Arrange
        String encodedPassword = bCryptPasswordEncoder.encode("password");
        User user = User.builder()
                .username("username")
                .password(encodedPassword)
                .roles(new HashSet<>())
                .build();
        User savedUser = userRepository.save(user);
        String jwt = jwtProvider.generateToken(savedUser, false);
        Token token = tokenRepository.save(
                Token.builder()
                        .token(jwt)
                        .invalidated(false)
                        .user(savedUser)
                        .build()
        );
        tokenRepository.save(token);
        passwordChangeRequestRepository.save(PasswordChangeRequest.builder()
                .newPassword("passw")
                .previousPassword("password")
                .user(savedUser)
                .code("0000")
                .build());
        ValidatePasswordChangeRequestDto validatePasswordChangeRequestDto = new ValidatePasswordChangeRequestDto("0000");
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/password/change/validate")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validatePasswordChangeRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void validateChangePassword_invalidCode_test() throws Exception {
        // Arrange
        String encodedPassword = bCryptPasswordEncoder.encode("password");
        User user = User.builder()
                .username("username")
                .password(encodedPassword)
                .roles(new HashSet<>())
                .build();
        User savedUser = userRepository.save(user);
        String jwt = jwtProvider.generateToken(savedUser, false);
        Token token = tokenRepository.save(
                Token.builder()
                        .token(jwt)
                        .invalidated(false)
                        .user(savedUser)
                        .build()
        );
        tokenRepository.save(token);
        passwordChangeRequestRepository.save(PasswordChangeRequest.builder()
                .user(savedUser)
                .newPassword("passw")
                .previousPassword("password")
                .code("0000")
                .build());
        ValidatePasswordChangeRequestDto validatePasswordChangeRequestDto = new ValidatePasswordChangeRequestDto("1111");
        // Act, Assert
        mockMvc.perform(post("/api/v1/auth/password/change/validate")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validatePasswordChangeRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
