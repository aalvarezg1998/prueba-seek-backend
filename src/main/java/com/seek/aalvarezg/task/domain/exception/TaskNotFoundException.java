package com.seek.aalvarezg.task.domain.exception;

import com.seek.aalvarezg.shared.domain.exception.DomainException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class TaskNotFoundException extends DomainException {

    public TaskNotFoundException(UUID taskId) {
        super("Task with id '" + taskId + "' not found", HttpStatus.NOT_FOUND);
    }
}
