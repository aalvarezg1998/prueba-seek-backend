package com.seek.aalvarezg.authentication.domain.port;

import com.seek.aalvarezg.authentication.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
    boolean existsByEmail(String email);
}
