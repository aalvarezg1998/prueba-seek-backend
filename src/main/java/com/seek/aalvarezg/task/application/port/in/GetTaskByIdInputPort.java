package com.seek.aalvarezg.task.application.port.in;

import com.seek.aalvarezg.task.application.dto.TaskResponse;

import java.util.UUID;

public interface GetTaskByIdInputPort {

    TaskResponse execute(UUID taskId, UUID userId);
}
