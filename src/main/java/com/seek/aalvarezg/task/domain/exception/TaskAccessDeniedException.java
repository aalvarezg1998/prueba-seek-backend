package com.seek.aalvarezg.task.domain.exception;

import com.seek.aalvarezg.shared.domain.exception.DomainException;
import com.seek.aalvarezg.shared.domain.exception.ErrorKind;

import java.util.UUID;

public class TaskAccessDeniedException extends DomainException {

    public TaskAccessDeniedException(UUID taskId) {
        super("You do not have permission to access task '" + taskId + "'", ErrorKind.FORBIDDEN);
    }
}
