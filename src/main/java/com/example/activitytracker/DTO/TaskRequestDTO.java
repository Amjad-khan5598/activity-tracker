package com.example.activitytracker.DTO;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TaskRequestDTO {
	@NotNull(message = "Date is required")
	private LocalDate date;
	
	@NotBlank(message = "Task Description is required")
	private String taskDescription;
	
	private boolean completed;
}
