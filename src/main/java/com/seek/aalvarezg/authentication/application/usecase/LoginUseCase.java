package com.seek.aalvarezg.authentication.application.usecase;

import com.seek.aalvarezg.authentication.application.command.LoginCommand;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.application.port.in.LoginInputPort;
import com.seek.aalvarezg.authentication.domain.exception.InvalidCredentialsException;
import com.seek.aalvarezg.authentication.domain.port.out.PasswordHasherPort;
import com.seek.aalvarezg.authentication.domain.port.out.TokenProviderPort;
import com.seek.aalvarezg.authentication.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase implements LoginInputPort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenProviderPort tokenProviderPort;

    @Override
    public AuthResponse execute(LoginCommand command) {
        var user = userRepositoryPort.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasherPort.matches(command.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenProviderPort.generate(user.getId());
        return new AuthResponse(token, user.getEmail());
    }
}
