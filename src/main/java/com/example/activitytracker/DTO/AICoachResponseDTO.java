package com.example.activitytracker.DTO;

import lombok.Data;

@Data
public class AICoachResponseDTO {

    private String summary;
    private String feedback;
    private String suggestion;
    private String motivation;
}