package com.seek.aalvarezg.task.infrastructure.adapter.out.persistence;

import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.model.TaskStatus;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import com.seek.aalvarezg.task.infrastructure.adapter.out.persistence.entity.TaskJpaEntity;
import com.seek.aalvarezg.task.infrastructure.adapter.out.persistence.repository.SpringDataTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskRepositoryJpaAdapter implements TaskRepositoryPort {

    private final SpringDataTaskRepository jpaRepository;

    @Override
    public List<Task> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Task save(Task task) {
        TaskJpaEntity entity = toEntity(task);
        TaskJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByIdAndUserId(UUID id, UUID userId) {
        return jpaRepository.existsByIdAndUserId(id, userId);
    }

    private Task toDomain(TaskJpaEntity entity) {
        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(TaskStatus.valueOf(entity.getStatus()))
                .userId(entity.getUserId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private TaskJpaEntity toEntity(Task task) {
        return TaskJpaEntity.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .userId(task.getUserId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
