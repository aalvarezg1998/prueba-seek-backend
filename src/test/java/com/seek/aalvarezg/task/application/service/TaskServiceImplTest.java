package com.seek.aalvarezg.task.application.service;

import com.seek.aalvarezg.task.application.dto.CreateTaskRequest;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.application.dto.UpdateTaskRequest;
import com.seek.aalvarezg.task.domain.exception.TaskAccessDeniedException;
import com.seek.aalvarezg.task.domain.exception.TaskNotFoundException;
import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.model.TaskStatus;
import com.seek.aalvarezg.task.domain.port.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskServiceImpl Tests")
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID userId;
    private UUID taskId;
    private Task task;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();
        task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.TODO)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("getAllTasks")
    class GetAllTasks {

        @Test
        @DisplayName("should return all tasks for user")
        void shouldReturnAllTasksForUser() {
            when(taskRepository.findAllByUserId(userId)).thenReturn(List.of(task));

            List<TaskResponse> result = taskService.getAllTasks(userId);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).title()).isEqualTo("Test Task");
            verify(taskRepository).findAllByUserId(userId);
        }

        @Test
        @DisplayName("should return empty list when no tasks")
        void shouldReturnEmptyListWhenNoTasks() {
            when(taskRepository.findAllByUserId(userId)).thenReturn(List.of());

            List<TaskResponse> result = taskService.getAllTasks(userId);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getTaskById")
    class GetTaskById {

        @Test
        @DisplayName("should return task when found and owned by user")
        void shouldReturnTaskWhenFoundAndOwned() {
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

            TaskResponse result = taskService.getTaskById(taskId, userId);

            assertThat(result.id()).isEqualTo(taskId);
            assertThat(result.title()).isEqualTo("Test Task");
        }

        @Test
        @DisplayName("should throw TaskNotFoundException when task not found")
        void shouldThrowWhenTaskNotFound() {
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> taskService.getTaskById(taskId, userId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("should throw TaskAccessDeniedException when task belongs to another user")
        void shouldThrowWhenAccessDenied() {
            UUID otherUserId = UUID.randomUUID();
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

            assertThatThrownBy(() -> taskService.getTaskById(taskId, otherUserId))
                    .isInstanceOf(TaskAccessDeniedException.class);
        }
    }

    @Nested
    @DisplayName("createTask")
    class CreateTask {

        @Test
        @DisplayName("should create and return a new task")
        void shouldCreateAndReturnTask() {
            CreateTaskRequest request = new CreateTaskRequest("New Task", "Description", "TODO");
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            TaskResponse result = taskService.createTask(request, userId);

            assertThat(result.title()).isEqualTo("New Task");
            assertThat(result.description()).isEqualTo("Description");
            assertThat(result.status()).isEqualTo("TODO");
            assertThat(result.id()).isNotNull();
            verify(taskRepository).save(any(Task.class));
        }
    }

    @Nested
    @DisplayName("updateTask")
    class UpdateTask {

        @Test
        @DisplayName("should update task when found and owned")
        void shouldUpdateTask() {
            UpdateTaskRequest request = new UpdateTaskRequest("Updated Title", null, "IN_PROGRESS");
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            TaskResponse result = taskService.updateTask(taskId, request, userId);

            assertThat(result.title()).isEqualTo("Updated Title");
            assertThat(result.status()).isEqualTo("IN_PROGRESS");
        }

        @Test
        @DisplayName("should throw TaskNotFoundException when task not found")
        void shouldThrowWhenTaskNotFound() {
            UpdateTaskRequest request = new UpdateTaskRequest("Updated", null, null);
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> taskService.updateTask(taskId, request, userId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("should throw TaskAccessDeniedException when not owned")
        void shouldThrowWhenNotOwned() {
            UUID otherUserId = UUID.randomUUID();
            UpdateTaskRequest request = new UpdateTaskRequest("Updated", null, null);
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

            assertThatThrownBy(() -> taskService.updateTask(taskId, request, otherUserId))
                    .isInstanceOf(TaskAccessDeniedException.class);
        }
    }

    @Nested
    @DisplayName("deleteTask")
    class DeleteTask {

        @Test
        @DisplayName("should delete task when found and owned")
        void shouldDeleteTask() {
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

            taskService.deleteTask(taskId, userId);

            verify(taskRepository).deleteById(taskId);
        }

        @Test
        @DisplayName("should throw TaskNotFoundException when task not found")
        void shouldThrowWhenTaskNotFound() {
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> taskService.deleteTask(taskId, userId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("should throw TaskAccessDeniedException when not owned")
        void shouldThrowWhenNotOwned() {
            UUID otherUserId = UUID.randomUUID();
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

            assertThatThrownBy(() -> taskService.deleteTask(taskId, otherUserId))
                    .isInstanceOf(TaskAccessDeniedException.class);
        }
    }
}
