package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.application.port.in.GetTaskByIdInputPort;
import com.seek.aalvarezg.task.domain.exception.TaskAccessDeniedException;
import com.seek.aalvarezg.task.domain.exception.TaskNotFoundException;
import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetTaskByIdUseCase implements GetTaskByIdInputPort {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public TaskResponse execute(UUID taskId, UUID userId) {
        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        if (!task.isOwnedBy(userId)) {
            throw new TaskAccessDeniedException(taskId);
        }
        return TaskResponseMapper.toResponse(task);
    }
}
