package com.seek.aalvarezg.task.application.port.in;

import com.seek.aalvarezg.task.application.dto.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface GetAllTasksInputPort {

    List<TaskResponse> execute(UUID userId);
}
