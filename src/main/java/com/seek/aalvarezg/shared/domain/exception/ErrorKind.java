package com.seek.aalvarezg.shared.domain.exception;

/**
 * Códigos de error de dominio. Agnóstico de transporte (HTTP, etc.).
 * La capa de infraestructura (p. ej. GlobalExceptionHandler) mapea a HttpStatus.
 */
public enum ErrorKind {
    CONFLICT,
    UNAUTHORIZED,
    NOT_FOUND,
    FORBIDDEN,
    UNPROCESSABLE,
    INTERNAL
}
