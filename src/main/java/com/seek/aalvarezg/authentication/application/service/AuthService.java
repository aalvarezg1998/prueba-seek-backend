package com.seek.aalvarezg.authentication.application.service;

import com.seek.aalvarezg.authentication.application.dto.AuthResponse;
import com.seek.aalvarezg.authentication.application.dto.LoginRequest;
import com.seek.aalvarezg.authentication.application.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
