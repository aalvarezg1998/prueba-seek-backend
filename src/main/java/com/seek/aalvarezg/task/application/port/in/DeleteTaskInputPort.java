package com.seek.aalvarezg.task.application.port.in;

import com.seek.aalvarezg.task.application.command.DeleteTaskCommand;

public interface DeleteTaskInputPort {

    void execute(DeleteTaskCommand command);
}
