package com.seek.aalvarezg.authentication.application.usecase;

import com.seek.aalvarezg.authentication.application.command.RegisterUserCommand;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.application.port.in.RegisterUserInputPort;
import com.seek.aalvarezg.authentication.domain.exception.UserAlreadyExistsException;
import com.seek.aalvarezg.authentication.domain.model.User;
import com.seek.aalvarezg.authentication.domain.port.out.PasswordHasherPort;
import com.seek.aalvarezg.authentication.domain.port.out.TokenProviderPort;
import com.seek.aalvarezg.authentication.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase implements RegisterUserInputPort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenProviderPort tokenProviderPort;

    @Override
    @Transactional
    public AuthResponse execute(RegisterUserCommand command) {
        if (userRepositoryPort.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException(command.email());
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(command.email())
                .password(passwordHasherPort.hash(command.password()))
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepositoryPort.save(user);
        String token = tokenProviderPort.generate(savedUser.getId());

        return new AuthResponse(token, savedUser.getEmail());
    }
}
