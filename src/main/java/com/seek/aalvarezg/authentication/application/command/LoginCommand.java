package com.seek.aalvarezg.authentication.application.command;

/**
 * Comando de aplicación para login. Sin anotaciones de validación.
 */
public record LoginCommand(String email, String password) {
}
