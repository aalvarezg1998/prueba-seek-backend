package com.seek.aalvarezg.authentication.domain.exception;

import com.seek.aalvarezg.shared.domain.exception.DomainException;
import com.seek.aalvarezg.shared.domain.exception.ErrorKind;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Invalid email or password", ErrorKind.UNAUTHORIZED);
    }
}
