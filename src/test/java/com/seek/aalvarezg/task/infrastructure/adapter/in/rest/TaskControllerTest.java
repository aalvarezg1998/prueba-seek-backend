package com.seek.aalvarezg.task.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seek.aalvarezg.authentication.domain.port.out.TokenProviderPort;
import com.seek.aalvarezg.task.application.port.in.CreateTaskInputPort;
import com.seek.aalvarezg.task.application.port.in.DeleteTaskInputPort;
import com.seek.aalvarezg.task.application.port.in.GetAllTasksInputPort;
import com.seek.aalvarezg.task.application.port.in.GetTaskByIdInputPort;
import com.seek.aalvarezg.task.application.port.in.UpdateTaskInputPort;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.domain.exception.TaskNotFoundException;
import com.seek.aalvarezg.task.infrastructure.adapter.in.rest.dto.CreateTaskRequestDto;
import com.seek.aalvarezg.task.infrastructure.adapter.in.rest.dto.UpdateTaskRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("TaskController Integration Tests")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenProviderPort tokenProviderPort;

    @MockBean
    private GetAllTasksInputPort getAllTasksInputPort;

    @MockBean
    private GetTaskByIdInputPort getTaskByIdInputPort;

    @MockBean
    private CreateTaskInputPort createTaskInputPort;

    @MockBean
    private UpdateTaskInputPort updateTaskInputPort;

    @MockBean
    private DeleteTaskInputPort deleteTaskInputPort;

    private UUID userId;
    private UUID taskId;
    private String jwtToken;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();
        jwtToken = tokenProviderPort.generate(userId);
        taskResponse = new TaskResponse(
                taskId, "Test Task", "Test Description", "TODO",
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("GET /api/v1/tasks should return all tasks")
    void getAllTasks_shouldReturnTasks() throws Exception {
        when(getAllTasksInputPort.execute(userId)).thenReturn(List.of(taskResponse));

        mockMvc.perform(get("/api/v1/tasks")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskId.toString()))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    @DisplayName("GET /api/v1/tasks without token should return 401")
    void getAllTasks_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} should return task")
    void getTaskById_shouldReturnTask() throws Exception {
        when(getTaskByIdInputPort.execute(taskId, userId)).thenReturn(taskResponse);

        mockMvc.perform(get("/api/v1/tasks/{id}", taskId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} should return 404 when not found")
    void getTaskById_whenNotFound_shouldReturn404() throws Exception {
        when(getTaskByIdInputPort.execute(taskId, userId)).thenThrow(new TaskNotFoundException(taskId));

        mockMvc.perform(get("/api/v1/tasks/{id}", taskId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/tasks should create task")
    void createTask_shouldReturnCreatedTask() throws Exception {
        CreateTaskRequestDto request = new CreateTaskRequestDto("New Task", "Description", "TODO");
        when(createTaskInputPort.execute(any(), eq(userId))).thenReturn(taskResponse);

        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskId.toString()));
    }

    @Test
    @DisplayName("POST /api/v1/tasks with invalid data should return 422")
    void createTask_withInvalidData_shouldReturn422() throws Exception {
        CreateTaskRequestDto request = new CreateTaskRequestDto("", "Description", "INVALID");

        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /api/v1/tasks/{id} should update task")
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        UpdateTaskRequestDto request = new UpdateTaskRequestDto("Updated", null, "IN_PROGRESS");
        TaskResponse updated = new TaskResponse(
                taskId, "Updated", "Test Description", "IN_PROGRESS",
                LocalDateTime.now(), LocalDateTime.now()
        );
        when(updateTaskInputPort.execute(eq(taskId), any(), eq(userId))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("DELETE /api/v1/tasks/{id} should return 204")
    void deleteTask_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        verify(deleteTaskInputPort).execute(any());
    }
}
