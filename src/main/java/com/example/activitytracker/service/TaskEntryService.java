package com.example.activitytracker.service;

import com.example.activitytracker.DTO.TaskPageResponseDTO;
import com.example.activitytracker.DTO.TaskRequestDTO;
import com.example.activitytracker.DTO.TaskResponseDTO;
import java.util.List;

public interface TaskEntryService {
		
	TaskResponseDTO createTask(TaskRequestDTO request);
	
	TaskPageResponseDTO getMyTasks(int page, int size);
	
	TaskResponseDTO getTaskById(Long taskid);
	
	TaskResponseDTO update(Long taskid, TaskRequestDTO request);
	
	void delete(Long taskid);
}
