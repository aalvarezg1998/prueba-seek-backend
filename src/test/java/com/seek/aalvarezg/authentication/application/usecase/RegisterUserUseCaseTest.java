package com.seek.aalvarezg.authentication.application.usecase;

import com.seek.aalvarezg.authentication.application.command.RegisterUserCommand;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.domain.exception.UserAlreadyExistsException;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserUseCase Tests")
class RegisterUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

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
    @DisplayName("should register user and return token")
    void shouldRegisterUserAndReturnToken() {
        RegisterUserCommand command = new RegisterUserCommand(email, password);
        when(userRepositoryPort.existsByEmail(email)).thenReturn(false);
        when(passwordHasherPort.hash(password)).thenReturn("encodedPassword");
        when(userRepositoryPort.save(any(User.class))).thenReturn(user);
        when(tokenProviderPort.generate(any(UUID.class))).thenReturn("jwt.token");

        AuthResponse response = registerUserUseCase.execute(command);

        assertThat(response.token()).isEqualTo("jwt.token");
        assertThat(response.email()).isEqualTo(email);
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    @DisplayName("should throw UserAlreadyExistsException when email exists")
    void shouldThrowWhenEmailExists() {
        RegisterUserCommand command = new RegisterUserCommand(email, password);
        when(userRepositoryPort.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> registerUserUseCase.execute(command))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining(email);
    }
}
