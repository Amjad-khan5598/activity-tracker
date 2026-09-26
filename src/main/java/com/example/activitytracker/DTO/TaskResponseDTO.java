package com.example.activitytracker.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponseDTO {
	 private Long id;
	    private LocalDate date;
	    private String taskDescription;
	    private boolean completed;
}
