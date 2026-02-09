package com.seek.aalvarezg.authentication.domain.port.out;

import java.util.UUID;

public interface TokenProviderPort {

    String generate(UUID userId);

    UUID extractSubject(String token);

    boolean isValid(String token);
}
