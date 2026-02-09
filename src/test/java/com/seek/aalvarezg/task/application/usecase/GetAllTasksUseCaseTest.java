package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.model.TaskStatus;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAllTasksUseCase Tests")
class GetAllTasksUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private GetAllTasksUseCase getAllTasksUseCase;

    @Test
    @DisplayName("should return all tasks for user")
    void shouldReturnAllTasksForUser() {
        UUID userId = UUID.randomUUID();
        Task task = Task.builder()
                .id(UUID.randomUUID()).title("Test Task").description("Desc")
                .status(TaskStatus.TODO).userId(userId)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
        when(taskRepositoryPort.findAllByUserId(userId)).thenReturn(List.of(task));

        List<TaskResponse> result = getAllTasksUseCase.execute(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Test Task");
        verify(taskRepositoryPort).findAllByUserId(userId);
    }

    @Test
    @DisplayName("should return empty list when no tasks")
    void shouldReturnEmptyListWhenNoTasks() {
        UUID userId = UUID.randomUUID();
        when(taskRepositoryPort.findAllByUserId(userId)).thenReturn(List.of());

        List<TaskResponse> result = getAllTasksUseCase.execute(userId);

        assertThat(result).isEmpty();
    }
}
