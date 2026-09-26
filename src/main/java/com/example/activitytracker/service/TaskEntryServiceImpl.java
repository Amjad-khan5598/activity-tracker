package com.example.activitytracker.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.example.activitytracker.DTO.TaskRequestDTO;
import com.example.activitytracker.DTO.TaskResponseDTO;
import com.example.activitytracker.model.TaskEntry;
import com.example.activitytracker.model.User;
import com.example.activitytracker.repository.TaskEntryRepository;
import com.example.activitytracker.repository.UserRepository;

@Service
public class TaskEntryServiceImpl implements TaskEntryService{
	
	private final TaskEntryRepository taskEntryRepository;
	private final UserRepository userRepository;
	
	private User getAuthenticatedUser() {
	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    return userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}
	
	public TaskEntryServiceImpl(TaskEntryRepository taskEntryRepository,UserRepository userRepository) {
		this.taskEntryRepository=taskEntryRepository;
		this.userRepository=userRepository;		
	}
	
	@Override
	public TaskResponseDTO createTask(TaskRequestDTO request) {

	    User user = getAuthenticatedUser();

	    TaskEntry task = new TaskEntry();

	    task.setDate(request.getDate());
	    task.setTaskDescription(request.getTaskDescription());
	    task.setCompleted(request.isCompleted());
	    task.setUser(user);

	    TaskEntry savedTask = taskEntryRepository.save(task);

	    return new TaskResponseDTO(
	            savedTask.getId(),
	            savedTask.getDate(),
	            savedTask.getTaskDescription(),
	            savedTask.isCompleted()
	    );
	}
	
	@Override
	public List<TaskResponseDTO> getMyTasks() {

	    User user = getAuthenticatedUser();

	    return taskEntryRepository.findByUser(user)
	            .stream()
	            .map(task -> new TaskResponseDTO(
	                    task.getId(),
	                    task.getDate(),
	                    task.getTaskDescription(),
	                    task.isCompleted()
	            ))
	            .toList();
	}
	@Override
	public TaskResponseDTO getTaskById(Long taskId) {

	    User user = getAuthenticatedUser();

	    TaskEntry task = taskEntryRepository.findById(taskId)
	            .orElseThrow(() -> new RuntimeException("Task not found"));

	    if (!task.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("You are not allowed to access this task");
	    }

	    return new TaskResponseDTO(
	            task.getId(),
	            task.getDate(),
	            task.getTaskDescription(),
	            task.isCompleted()
	    );
	}
	@Override
	public TaskResponseDTO update(Long taskId, TaskRequestDTO request) {

	    User user = getAuthenticatedUser();

	    TaskEntry task = taskEntryRepository.findById(taskId)
	            .orElseThrow(() -> new RuntimeException("Task not found"));

	    if (!task.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("You are not allowed to update this task");
	    }

	    task.setDate(request.getDate());
	    task.setTaskDescription(request.getTaskDescription());
	    task.setCompleted(request.isCompleted());

	    TaskEntry updatedTask = taskEntryRepository.save(task);

	    return new TaskResponseDTO(
	            updatedTask.getId(),
	            updatedTask.getDate(),
	            updatedTask.getTaskDescription(),
	            updatedTask.isCompleted()
	    );
	}
	@Override
	public void delete(Long taskId) {

	    User user = getAuthenticatedUser();

	    TaskEntry task = taskEntryRepository.findById(taskId)
	            .orElseThrow(() -> new RuntimeException("Task not found"));

	    if (!task.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("You are not allowed to delete this task");
	    }

	    taskEntryRepository.delete(task);
	}
	
}
