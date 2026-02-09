package com.seek.aalvarezg.authentication.application.usecase;

import com.seek.aalvarezg.authentication.application.command.LoginCommand;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.domain.exception.InvalidCredentialsException;
import com.seek.aalvarezg.authentication.domain.model.User;
import com.seek.aalvarezg.authentication.domain.port.out.PasswordHasherPort;
import com.seek.aalvarezg.authentication.domain.port.out.TokenProviderPort;
import com.seek.aalvarezg.authentication.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginUseCase Tests")
class LoginUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @InjectMocks
    private LoginUseCase loginUseCase;

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

    @Test
    @DisplayName("should login and return token with valid credentials")
    void shouldLoginWithValidCredentials() {
        LoginCommand command = new LoginCommand(email, password);
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordHasherPort.matches(password, "encodedPassword")).thenReturn(true);
        when(tokenProviderPort.generate(user.getId())).thenReturn("jwt.token");

        AuthResponse response = loginUseCase.execute(command);

        assertThat(response.token()).isEqualTo("jwt.token");
        assertThat(response.email()).isEqualTo(email);
    }

    @Test
    @DisplayName("should throw InvalidCredentialsException when user not found")
    void shouldThrowWhenUserNotFound() {
        LoginCommand command = new LoginCommand(email, password);
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUseCase.execute(command))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("should throw InvalidCredentialsException when password is wrong")
    void shouldThrowWhenPasswordIsWrong() {
        LoginCommand command = new LoginCommand(email, "wrongpassword");
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordHasherPort.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThatThrownBy(() -> loginUseCase.execute(command))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
