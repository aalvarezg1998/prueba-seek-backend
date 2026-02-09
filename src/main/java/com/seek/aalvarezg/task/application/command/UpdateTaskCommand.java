package com.seek.aalvarezg.task.application.command;

/**
 * Comando de aplicación para actualizar una tarea. Campos opcionales (null = no actualizar).
 */
public record UpdateTaskCommand(String title, String description, String status) {
}
