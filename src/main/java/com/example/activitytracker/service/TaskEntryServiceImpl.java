package com.example.activitytracker.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.activitytracker.DTO.TaskPageResponseDTO;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.example.activitytracker.DTO.TaskRequestDTO;
import com.example.activitytracker.DTO.TaskResponseDTO;
import com.example.activitytracker.exception.TaskAccessDeniedException;
import com.example.activitytracker.exception.TaskNotFoundException;
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
	public TaskPageResponseDTO getMyTasks(int page, int size) {

	    User user = getAuthenticatedUser();

	    // Protect the API from invalid or excessively large page requests.
	    if (page < 0) {
	        throw new IllegalArgumentException("Page must not be negative");
	    }

	    if (size < 1 || size > 100) {
	        throw new IllegalArgumentException(
	                "Page size must be between 1 and 100");
	    }

	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            Sort.by("id").ascending()
	    );

	    Page<TaskEntry> taskPage =
	            taskEntryRepository.findByUser(user, pageable);

	    List<TaskResponseDTO> tasks = taskPage.getContent()
	            .stream()
	            .map(task -> new TaskResponseDTO(
	                    task.getId(),
	                    task.getDate(),
	                    task.getTaskDescription(),
	                    task.isCompleted()
	            ))
	            .toList();

	    return new TaskPageResponseDTO(
	            tasks,
	            taskPage.getNumber(),
	            taskPage.getSize(),
	            taskPage.getTotalElements(),
	            taskPage.getTotalPages(),
	            taskPage.isLast()
	    );
	}
	@Override
	public TaskResponseDTO getTaskById(Long taskId) {

	    User user = getAuthenticatedUser();

	    TaskEntry task = taskEntryRepository.findById(taskId)
	            .orElseThrow(() -> new TaskNotFoundException("Task not found"));

	    if (!task.getUser().getId().equals(user.getId())) {
	        throw new TaskAccessDeniedException("You are not allowed to access this task");
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
	            .orElseThrow(() -> new TaskNotFoundException("Task not found"));

	    if (!task.getUser().getId().equals(user.getId())) {
	        throw new TaskAccessDeniedException("You are not allowed to update this task");
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
	            .orElseThrow(() -> new TaskNotFoundException("Task not found"));

	    if (!task.getUser().getId().equals(user.getId())) {
	        throw new TaskAccessDeniedException("You are not allowed to delete this task");
	    }

	    taskEntryRepository.delete(task);
	}
	
}
