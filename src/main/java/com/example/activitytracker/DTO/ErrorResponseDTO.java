package com.example.activitytracker.DTO;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private int status;
    private String error;
    private String message;
    private Map<String, String> fieldErrors;

    public ErrorResponseDTO(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = null;
    }
}
