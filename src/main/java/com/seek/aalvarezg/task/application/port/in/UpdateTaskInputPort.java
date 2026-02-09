package com.seek.aalvarezg.task.application.port.in;

import com.seek.aalvarezg.task.application.command.UpdateTaskCommand;
import com.seek.aalvarezg.task.application.dto.TaskResponse;

import java.util.UUID;

public interface UpdateTaskInputPort {

    TaskResponse execute(UUID taskId, UpdateTaskCommand command, UUID userId);
}
