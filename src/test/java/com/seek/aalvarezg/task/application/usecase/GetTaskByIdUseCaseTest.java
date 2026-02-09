package com.seek.aalvarezg.task.application.usecase;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetTaskByIdUseCase Tests")
class GetTaskByIdUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private GetTaskByIdUseCase getTaskByIdUseCase;

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
    @DisplayName("should return task when found and owned by user")
    void shouldReturnTaskWhenFoundAndOwned() {
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));

        TaskResponse result = getTaskByIdUseCase.execute(taskId, userId);

        assertThat(result.id()).isEqualTo(taskId);
        assertThat(result.title()).isEqualTo("Test Task");
    }

    @Test
    @DisplayName("should throw TaskNotFoundException when task not found")
    void shouldThrowWhenTaskNotFound() {
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getTaskByIdUseCase.execute(taskId, userId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("should throw TaskAccessDeniedException when task belongs to another user")
    void shouldThrowWhenAccessDenied() {
        UUID otherUserId = UUID.randomUUID();
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> getTaskByIdUseCase.execute(taskId, otherUserId))
                .isInstanceOf(TaskAccessDeniedException.class);
    }
}
