package com.seek.aalvarezg.task.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, UUID> {
    List<TaskJpaEntity> findAllByUserId(UUID userId);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}
