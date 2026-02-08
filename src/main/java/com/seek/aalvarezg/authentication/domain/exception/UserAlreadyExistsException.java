package com.seek.aalvarezg.authentication.domain.exception;

import com.seek.aalvarezg.shared.domain.exception.DomainException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists", HttpStatus.CONFLICT);
    }
}
