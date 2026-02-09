package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.command.UpdateTaskCommand;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.domain.exception.TaskAccessDeniedException;
import com.seek.aalvarezg.task.domain.exception.TaskNotFoundException;
import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.model.TaskStatus;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTaskUseCase Tests")
class UpdateTaskUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private UpdateTaskUseCase updateTaskUseCase;

    private UUID userId;
    private UUID taskId;
    private Task task;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();
        task = Task.builder()
                .id(taskId).title("Test Task").description("Test Description")
                .status(TaskStatus.TODO).userId(userId)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("should update task when found and owned")
    void shouldUpdateTask() {
        UpdateTaskCommand command = new UpdateTaskCommand("Updated Title", null, "IN_PROGRESS");
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse result = updateTaskUseCase.execute(taskId, command, userId);

        assertThat(result.title()).isEqualTo("Updated Title");
        assertThat(result.status()).isEqualTo("IN_PROGRESS");
    }

    @Test
    @DisplayName("should throw TaskNotFoundException when task not found")
    void shouldThrowWhenTaskNotFound() {
        UpdateTaskCommand command = new UpdateTaskCommand("Updated", null, null);
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateTaskUseCase.execute(taskId, command, userId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("should throw TaskAccessDeniedException when not owned")
    void shouldThrowWhenNotOwned() {
        UUID otherUserId = UUID.randomUUID();
        UpdateTaskCommand command = new UpdateTaskCommand("Updated", null, null);
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> updateTaskUseCase.execute(taskId, command, otherUserId))
                .isInstanceOf(TaskAccessDeniedException.class);
    }
}
