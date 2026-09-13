package com.example.activitytracker.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskEntryDTO {
	private Long id;
	private LocalDate date;
	private String taskDescription;
	private boolean completed;
	private Long userId;
}
