package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.domain.model.Task;

final class TaskResponseMapper {

    private TaskResponseMapper() {
    }

    static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
