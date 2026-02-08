package com.seek.aalvarezg.authentication.application.service;

import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.application.dto.LoginRequest;
import com.seek.aalvarezg.authentication.application.dto.RegisterRequest;
import com.seek.aalvarezg.authentication.domain.exception.InvalidCredentialsException;
import com.seek.aalvarezg.authentication.domain.exception.UserAlreadyExistsException;
import com.seek.aalvarezg.authentication.domain.model.User;
import com.seek.aalvarezg.authentication.domain.port.UserRepository;
import com.seek.aalvarezg.authentication.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Tests")
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private String email;
    private String password;
    private User user;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
        password = "password123";
        user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("encodedPassword")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("should register user and return token")
        void shouldRegisterUserAndReturnToken() {
            RegisterRequest request = new RegisterRequest(email, password);
            when(userRepository.existsByEmail(email)).thenReturn(false);
            when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(jwtService.generateToken(any(UUID.class))).thenReturn("jwt.token");

            AuthResponse response = authService.register(request);

            assertThat(response.token()).isEqualTo("jwt.token");
            assertThat(response.email()).isEqualTo(email);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should throw UserAlreadyExistsException when email exists")
        void shouldThrowWhenEmailExists() {
            RegisterRequest request = new RegisterRequest(email, password);
            when(userRepository.existsByEmail(email)).thenReturn(true);

            assertThatThrownBy(() -> authService.register(request))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining(email);
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("should login and return token with valid credentials")
        void shouldLoginWithValidCredentials() {
            LoginRequest request = new LoginRequest(email, password);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
            when(jwtService.generateToken(user.getId())).thenReturn("jwt.token");

            AuthResponse response = authService.login(request);

            assertThat(response.token()).isEqualTo("jwt.token");
            assertThat(response.email()).isEqualTo(email);
        }

        @Test
        @DisplayName("should throw InvalidCredentialsException when user not found")
        void shouldThrowWhenUserNotFound() {
            LoginRequest request = new LoginRequest(email, password);
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("should throw InvalidCredentialsException when password is wrong")
        void shouldThrowWhenPasswordIsWrong() {
            LoginRequest request = new LoginRequest(email, "wrongpassword");
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(InvalidCredentialsException.class);
        }
    }
}
