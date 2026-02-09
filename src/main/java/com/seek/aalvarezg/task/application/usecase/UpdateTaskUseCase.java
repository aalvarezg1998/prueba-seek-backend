package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.command.UpdateTaskCommand;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.application.port.in.UpdateTaskInputPort;
import com.seek.aalvarezg.task.domain.exception.TaskAccessDeniedException;
import com.seek.aalvarezg.task.domain.exception.TaskNotFoundException;
import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.model.TaskStatus;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateTaskUseCase implements UpdateTaskInputPort {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    @Transactional
    public TaskResponse execute(UUID taskId, UpdateTaskCommand command, UUID userId) {
        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        if (!task.isOwnedBy(userId)) {
            throw new TaskAccessDeniedException(taskId);
        }

        if (command.title() != null) {
            task.setTitle(command.title());
        }
        if (command.description() != null) {
            task.setDescription(command.description());
        }
        if (command.status() != null) {
            task.setStatus(TaskStatus.valueOf(command.status()));
        }
        task.setUpdatedAt(LocalDateTime.now());

        Task updated = taskRepositoryPort.save(task);
        return TaskResponseMapper.toResponse(updated);
    }
}
