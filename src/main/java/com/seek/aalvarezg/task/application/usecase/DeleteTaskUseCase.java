package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.command.DeleteTaskCommand;
import com.seek.aalvarezg.task.application.port.in.DeleteTaskInputPort;
import com.seek.aalvarezg.task.domain.exception.TaskAccessDeniedException;
import com.seek.aalvarezg.task.domain.exception.TaskNotFoundException;
import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteTaskUseCase implements DeleteTaskInputPort {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    @Transactional
    public void execute(DeleteTaskCommand command) {
        Task task = taskRepositoryPort.findById(command.taskId())
                .orElseThrow(() -> new TaskNotFoundException(command.taskId()));
        if (!task.isOwnedBy(command.userId())) {
            throw new TaskAccessDeniedException(command.taskId());
        }
        taskRepositoryPort.deleteById(command.taskId());
    }
}
