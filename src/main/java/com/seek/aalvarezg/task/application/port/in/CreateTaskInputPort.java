package com.seek.aalvarezg.task.application.port.in;

import com.seek.aalvarezg.task.application.command.CreateTaskCommand;
import com.seek.aalvarezg.task.application.dto.TaskResponse;

import java.util.UUID;

public interface CreateTaskInputPort {

    TaskResponse execute(CreateTaskCommand command, UUID userId);
}
