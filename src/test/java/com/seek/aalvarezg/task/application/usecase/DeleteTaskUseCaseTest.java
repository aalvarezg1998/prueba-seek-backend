package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.command.DeleteTaskCommand;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteTaskUseCase Tests")
class DeleteTaskUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private DeleteTaskUseCase deleteTaskUseCase;

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
    @DisplayName("should delete task when found and owned")
    void shouldDeleteTask() {
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));

        deleteTaskUseCase.execute(new DeleteTaskCommand(taskId, userId));

        verify(taskRepositoryPort).deleteById(taskId);
    }

    @Test
    @DisplayName("should throw TaskNotFoundException when task not found")
    void shouldThrowWhenTaskNotFound() {
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteTaskUseCase.execute(new DeleteTaskCommand(taskId, userId)))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("should throw TaskAccessDeniedException when not owned")
    void shouldThrowWhenNotOwned() {
        UUID otherUserId = UUID.randomUUID();
        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> deleteTaskUseCase.execute(new DeleteTaskCommand(taskId, otherUserId)))
                .isInstanceOf(TaskAccessDeniedException.class);
    }
}
