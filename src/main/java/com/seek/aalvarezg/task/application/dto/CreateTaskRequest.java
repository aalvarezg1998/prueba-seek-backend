package com.seek.aalvarezg.task.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @NotNull(message = "Status is required")
        @Pattern(regexp = "^(TODO|IN_PROGRESS|COMPLETED)$", message = "Status must be TODO, IN_PROGRESS, or COMPLETED")
        String status
) {
}
