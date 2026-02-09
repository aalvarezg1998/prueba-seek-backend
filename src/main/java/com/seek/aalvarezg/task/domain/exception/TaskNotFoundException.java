package com.seek.aalvarezg.task.domain.exception;

import com.seek.aalvarezg.shared.domain.exception.DomainException;
import com.seek.aalvarezg.shared.domain.exception.ErrorKind;

import java.util.UUID;

public class TaskNotFoundException extends DomainException {

    public TaskNotFoundException(UUID taskId) {
        super("Task with id '" + taskId + "' not found", ErrorKind.NOT_FOUND);
    }
}
