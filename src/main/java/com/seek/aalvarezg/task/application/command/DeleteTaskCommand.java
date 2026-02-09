package com.seek.aalvarezg.task.application.command;

import java.util.UUID;

/**
 * Comando de aplicación para eliminar una tarea (incluye taskId y userId para autorización).
 */
public record DeleteTaskCommand(UUID taskId, UUID userId) {
}
