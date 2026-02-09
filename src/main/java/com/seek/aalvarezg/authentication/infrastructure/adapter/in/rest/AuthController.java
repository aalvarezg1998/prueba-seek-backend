package com.seek.aalvarezg.authentication.infrastructure.adapter.in.rest;

import com.seek.aalvarezg.authentication.application.command.LoginCommand;
import com.seek.aalvarezg.authentication.application.command.RegisterUserCommand;
import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.application.port.in.LoginInputPort;
import com.seek.aalvarezg.authentication.application.port.in.RegisterUserInputPort;
import com.seek.aalvarezg.authentication.infrastructure.adapter.in.rest.dto.LoginRequestDto;
import com.seek.aalvarezg.authentication.infrastructure.adapter.in.rest.dto.RegisterRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and authentication endpoints")
public class AuthController {

    private final RegisterUserInputPort registerUserInputPort;
    private final LoginInputPort loginInputPort;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequestDto request) {
        RegisterUserCommand command = new RegisterUserCommand(request.email(), request.password());
        AuthResponse response = registerUserInputPort.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and get JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto request) {
        LoginCommand command = new LoginCommand(request.email(), request.password());
        AuthResponse response = loginInputPort.execute(command);
        return ResponseEntity.ok(response);
    }
}
