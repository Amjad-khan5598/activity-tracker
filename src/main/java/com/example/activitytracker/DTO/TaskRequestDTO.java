package com.example.activitytracker.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskRequestDTO {
	private LocalDate date;
	private String taskDescription;
	private boolean completed;
}
