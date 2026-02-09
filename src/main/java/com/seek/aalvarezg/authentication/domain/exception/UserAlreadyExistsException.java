package com.seek.aalvarezg.authentication.domain.exception;

import com.seek.aalvarezg.shared.domain.exception.DomainException;
import com.seek.aalvarezg.shared.domain.exception.ErrorKind;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists", ErrorKind.CONFLICT);
    }
}
