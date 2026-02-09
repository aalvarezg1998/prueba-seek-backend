package com.seek.aalvarezg.authentication.domain.port.out;

import com.seek.aalvarezg.authentication.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    User save(User user);
    boolean existsByEmail(String email);
}
