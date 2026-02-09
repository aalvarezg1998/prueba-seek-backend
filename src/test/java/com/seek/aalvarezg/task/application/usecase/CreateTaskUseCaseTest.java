package com.seek.aalvarezg.task.application.usecase;

import com.seek.aalvarezg.task.application.command.CreateTaskCommand;
import com.seek.aalvarezg.task.application.dto.TaskResponse;
import com.seek.aalvarezg.task.domain.model.Task;
import com.seek.aalvarezg.task.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTaskUseCase Tests")
class CreateTaskUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private CreateTaskUseCase createTaskUseCase;

    @Test
    @DisplayName("should create and return a new task")
    void shouldCreateAndReturnTask() {
        UUID userId = UUID.randomUUID();
        CreateTaskCommand command = new CreateTaskCommand("New Task", "Description", "TODO");
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse result = createTaskUseCase.execute(command, userId);

        assertThat(result.title()).isEqualTo("New Task");
        assertThat(result.description()).isEqualTo("Description");
        assertThat(result.status()).isEqualTo("TODO");
        assertThat(result.id()).isNotNull();
        verify(taskRepositoryPort).save(any(Task.class));
    }
}
