package com.example.activitytracker.DTO;

import lombok.Data;

@Data
public class ProgressResponseDTO {

    private long totalTasks;
    private long completedTasks;
    private double completionRate;
    private long currentStreak;
    private long tasksToday;
    private long completedToday;
}