package com.seek.aalvarezg.task.application.command;

/**
 * Comando de aplicación para crear una tarea. Sin anotaciones de validación.
 */
public record CreateTaskCommand(String title, String description, String status) {
}
