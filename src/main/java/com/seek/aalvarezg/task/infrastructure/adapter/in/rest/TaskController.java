package com.seek.aalvarezg.task.infrastructure.adapter.in.rest;

import com.seek.aalvarezg.task.application.command.CreateTaskCommand;
import com.seek.aalvarezg.task.application.command.DeleteTaskCommand;
import com.seek.aalvarezg.task.application.command.UpdateTaskCommand;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.application.port.in.CreateTaskInputPort;
import com.seek.aalvarezg.task.application.port.in.DeleteTaskInputPort;
import com.seek.aalvarezg.task.application.port.in.GetAllTasksInputPort;
import com.seek.aalvarezg.task.application.port.in.GetTaskByIdInputPort;
import com.seek.aalvarezg.task.application.port.in.UpdateTaskInputPort;
import com.seek.aalvarezg.task.infrastructure.adapter.in.rest.dto.CreateTaskRequestDto;
import com.seek.aalvarezg.task.infrastructure.adapter.in.rest.dto.UpdateTaskRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final GetAllTasksInputPort getAllTasksInputPort;
    private final GetTaskByIdInputPort getTaskByIdInputPort;
    private final CreateTaskInputPort createTaskInputPort;
    private final UpdateTaskInputPort updateTaskInputPort;
    private final DeleteTaskInputPort deleteTaskInputPort;

    @GetMapping
    @Operation(summary = "Get all tasks for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<TaskResponse>> getAllTasks(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(getAllTasksInputPort.execute(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(getTaskByIdInputPort.execute(id, userId));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "422", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequestDto request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        CreateTaskCommand command = new CreateTaskCommand(request.title(), request.description(), request.status());
        TaskResponse response = createTaskInputPort.execute(command, userId);
        return ResponseEntity
                .created(URI.create("/api/v1/tasks/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "422", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequestDto request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        UpdateTaskCommand command = new UpdateTaskCommand(request.title(), request.description(), request.status());
        return ResponseEntity.ok(updateTaskInputPort.execute(id, command, userId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        deleteTaskInputPort.execute(new DeleteTaskCommand(id, userId));
        return ResponseEntity.noContent().build();
    }
}
