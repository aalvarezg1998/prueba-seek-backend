package com.seek.aalvarezg.authentication.domain.exception;

import com.seek.aalvarezg.shared.domain.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }
}
