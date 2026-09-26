package com.example.activitytracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activitytracker.DTO.TaskRequestDTO;
import com.example.activitytracker.DTO.TaskResponseDTO;
import com.example.activitytracker.service.TaskEntryService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskEntryService taskEntryService;

    public TaskController(TaskEntryService taskEntryService) {
        this.taskEntryService = taskEntryService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @RequestBody TaskRequestDTO request) {

        TaskResponseDTO response = taskEntryService.createTask(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getMyTasks() {

        List<TaskResponseDTO> tasks = taskEntryService.getMyTasks();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @PathVariable Long taskId) {

        TaskResponseDTO response = taskEntryService.getTaskById(taskId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO request) {

        TaskResponseDTO response =
                taskEntryService.update(taskId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId) {

        taskEntryService.delete(taskId);

        return ResponseEntity.noContent().build();
    }
}