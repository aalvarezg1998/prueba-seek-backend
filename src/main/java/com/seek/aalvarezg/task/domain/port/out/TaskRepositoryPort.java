package com.seek.aalvarezg.task.domain.port.out;

import com.seek.aalvarezg.task.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    List<Task> findAllByUserId(UUID userId);
    Optional<Task> findById(UUID id);
    Task save(Task task);
    void deleteById(UUID id);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}
