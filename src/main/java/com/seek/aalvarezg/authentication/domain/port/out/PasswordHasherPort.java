package com.seek.aalvarezg.authentication.domain.port.out;

public interface PasswordHasherPort {

    String hash(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
