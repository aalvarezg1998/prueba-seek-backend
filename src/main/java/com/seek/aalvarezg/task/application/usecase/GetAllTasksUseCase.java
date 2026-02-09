package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.application.port.in.GetAllTasksInputPort;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllTasksUseCase implements GetAllTasksInputPort {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public List<TaskResponse> execute(UUID userId) {
        return taskRepositoryPort.findAllByUserId(userId).stream()
                .map(TaskResponseMapper::toResponse)
                .toList();
    }
}
