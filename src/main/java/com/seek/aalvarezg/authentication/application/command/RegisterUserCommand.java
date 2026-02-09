package com.seek.aalvarezg.authentication.application.command;

/**
 * Comando de aplicación para registro de usuario. Sin anotaciones de validación.
 */
public record RegisterUserCommand(String email, String password) {
}
