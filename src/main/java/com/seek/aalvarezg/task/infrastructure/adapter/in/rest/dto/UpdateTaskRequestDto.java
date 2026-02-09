package com.seek.aalvarezg.task.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequestDto(
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @Pattern(regexp = "^(TODO|IN_PROGRESS|COMPLETED)$", message = "Status must be TODO, IN_PROGRESS, or COMPLETED")
        String status
) {
}
