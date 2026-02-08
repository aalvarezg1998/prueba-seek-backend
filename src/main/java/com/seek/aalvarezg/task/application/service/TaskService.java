package com.seek.aalvarezg.task.application.service;

import com.seek.aalvarezg.task.application.dto.CreateTaskRequest;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.application.dto.UpdateTaskRequest;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponse> getAllTasks(UUID userId);
    TaskResponse getTaskById(UUID taskId, UUID userId);
    TaskResponse createTask(CreateTaskRequest request, UUID userId);
    TaskResponse updateTask(UUID taskId, UpdateTaskRequest request, UUID userId);
    void deleteTask(UUID taskId, UUID userId);
}
