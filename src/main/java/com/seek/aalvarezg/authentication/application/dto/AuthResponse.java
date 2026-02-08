package com.seek.aalvarezg.authentication.application.dto;

public record AuthResponse(
        String token,
        String email
) {
}
