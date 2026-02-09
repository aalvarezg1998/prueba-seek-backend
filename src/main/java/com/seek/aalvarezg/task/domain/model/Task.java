package com.seek.aalvarezg.task.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Política de dominio: solo el propietario puede acceder o modificar la tarea.
     */
    public boolean isOwnedBy(UUID userId) {
        return Objects.equals(this.userId, userId);
    }
}
