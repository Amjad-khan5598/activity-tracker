package com.example.activitytracker.service;

import com.example.activitytracker.DTO.TaskRequestDTO;
import com.example.activitytracker.DTO.TaskResponseDTO;
import java.util.List;

public interface TaskEntryService {
		
	TaskResponseDTO createTask(TaskRequestDTO request);
	
	List<TaskResponseDTO> getMyTasks();
	
	TaskResponseDTO getTaskById(Long taskid);
	
	TaskResponseDTO update(Long taskid, TaskRequestDTO request);
	
	void delete(Long taskid);
}
