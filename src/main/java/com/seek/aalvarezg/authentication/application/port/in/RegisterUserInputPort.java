package com.seek.aalvarezg.authentication.application.port.in;

import com.seek.aalvarezg.authentication.application.command.RegisterUserCommand;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;

public interface RegisterUserInputPort {

    AuthResponse execute(RegisterUserCommand command);
}
