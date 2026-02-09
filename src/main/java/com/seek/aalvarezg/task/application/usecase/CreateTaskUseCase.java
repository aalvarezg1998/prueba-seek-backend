package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.command.CreateTaskCommand;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.application.port.in.CreateTaskInputPort;
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
public class CreateTaskUseCase implements CreateTaskInputPort {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    @Transactional
    public TaskResponse execute(CreateTaskCommand command, UUID userId) {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .title(command.title())
                .description(command.description())
                .status(TaskStatus.valueOf(command.status()))
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Task saved = taskRepositoryPort.save(task);
        return TaskResponseMapper.toResponse(saved);
    }
}
