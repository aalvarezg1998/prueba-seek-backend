package com.seek.aalvarezg.shared.infrastructure.exception;

import com.seek.aalvarezg.shared.domain.exception.ErrorKind;
import org.springframework.http.HttpStatus;

/**
 * Mapea códigos de dominio (ErrorKind) a HttpStatus.
 * Único punto donde el "borde" HTTP conoce la correspondencia.
 */
public final class ErrorKindToHttpStatusMapper {

    private ErrorKindToHttpStatusMapper() {
    }

    public static HttpStatus toHttpStatus(ErrorKind errorKind) {
        return switch (errorKind) {
            case CONFLICT -> HttpStatus.CONFLICT;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case UNPROCESSABLE -> HttpStatus.UNPROCESSABLE_ENTITY;
            case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
