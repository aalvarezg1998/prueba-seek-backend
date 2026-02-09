package com.seek.aalvarezg.shared.domain.exception;

import lombok.Getter;

/**
 * Excepción base de dominio. No depende de ningún framework (p. ej. Spring).
 * El tipo de error se expresa con ErrorKind; la traducción a HTTP es responsabilidad del adaptador.
 */
@Getter
public abstract class DomainException extends RuntimeException {

    private final ErrorKind errorKind;

    protected DomainException(String message, ErrorKind errorKind) {
        super(message);
        this.errorKind = errorKind;
    }
}
