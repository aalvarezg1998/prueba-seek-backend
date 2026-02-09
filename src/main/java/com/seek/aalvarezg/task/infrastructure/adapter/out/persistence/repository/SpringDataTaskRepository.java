package com.seek.aalvarezg.task.infrastructure.adapter.out.persistence.repository;

import com.seek.aalvarezg.task.infrastructure.adapter.out.persistence.entity.TaskJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, UUID> {
    List<TaskJpaEntity> findAllByUserId(UUID userId);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}
