package com.seek.aalvarezg.authentication.application.port.in;

import com.seek.aalvarezg.authentication.application.command.LoginCommand;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;

public interface LoginInputPort {

    AuthResponse execute(LoginCommand command);
}
