package com.example.activitytracker.service;

import com.example.activitytracker.DTO.AICoachResponseDTO;
import com.example.activitytracker.DTO.ProgressResponseDTO;
import com.example.activitytracker.exception.UserNotFoundException;
import com.example.activitytracker.model.TaskEntry;
import com.example.activitytracker.model.User;
import com.example.activitytracker.repository.TaskEntryRepository;
import com.example.activitytracker.repository.UserRepository;
import com.google.genai.Client;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AICoachServiceImpl implements AICoachService{
	 private final Client geminiClient;
	 private final ProgressService progressService;
	 private final TaskEntryRepository taskEntryRepository;
	 private final UserRepository userRepository;

	    public AICoachServiceImpl(Client geminiClient,ProgressService progressService,TaskEntryRepository taskEntryRepository,UserRepository userRepository) {
	        this.geminiClient = geminiClient;
	        this.progressService=progressService;
	        this.taskEntryRepository=taskEntryRepository;
	        this.userRepository=userRepository;
	    }

	    @Override
	    public AICoachResponseDTO getCoaching() {

	        Authentication authentication =
	                SecurityContextHolder.getContext().getAuthentication();

	        String email = authentication.getName();

	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new UserNotFoundException("User not found"));

	        List<TaskEntry> tasks = taskEntryRepository.findByUser(user);

	        System.out.println("Number of tasks retrieved: " + tasks.size());

	        for (TaskEntry task : tasks) {
	            System.out.println(
	                    "Task: " + task.getTaskDescription()
	                    + " | Date: " + task.getDate()
	                    + " | Completed: " + task.isCompleted()
	            );
	        }

	        System.out.println("AI Coach requested by: " + email);

	        ProgressResponseDTO progress = progressService.getProgress();
	        String prompt = buildPrompt(progress, tasks);

	        System.out.println("----- AI PROMPT -----");
	        System.out.println(prompt);
	        System.out.println("--------------------");

	        System.out.println("Total tasks: " + progress.getTotalTasks());
	        System.out.println("Completed tasks: " + progress.getCompletedTasks());
	        System.out.println("Completion rate: " + progress.getCompletionRate());
	        System.out.println("Current streak: " + progress.getCurrentStreak());
	        System.out.println("Tasks today: " + progress.getTasksToday());
	        System.out.println("Completed today: " + progress.getCompletedToday());

	        String response = geminiClient.models.generateContent(
	                "gemini-3.5-flash-lite",
	                prompt,
	                null
	        ).text();

	        System.out.println("Gemini response: " + response);

	        return new AICoachResponseDTO();
	    }
	    private String buildPrompt(ProgressResponseDTO progress, List<TaskEntry> tasks) {

	        StringBuilder prompt = new StringBuilder();

	        prompt.append("""
	                You are an AI productivity coach.

	                Analyze the user's productivity data and provide practical,
	                personalized feedback.

	                User Progress:
	                """);

	        prompt.append("Total tasks: ")
	                .append(progress.getTotalTasks())
	                .append("\n");

	        prompt.append("Completed tasks: ")
	                .append(progress.getCompletedTasks())
	                .append("\n");

	        prompt.append("Completion rate: ")
	                .append(String.format("%.2f", progress.getCompletionRate()))
	                .append("%\n");

	        prompt.append("Current streak: ")
	                .append(progress.getCurrentStreak())
	                .append(" days\n");

	        prompt.append("Tasks today: ")
	                .append(progress.getTasksToday())
	                .append("\n");

	        prompt.append("Completed today: ")
	                .append(progress.getCompletedToday())
	                .append("\n\n");

	        prompt.append("Task History:\n");

	        for (TaskEntry task : tasks) {
	            prompt.append("- ")
	                    .append(task.getDate())
	                    .append(" | ")
	                    .append(task.getTaskDescription())
	                    .append(" | ")
	                    .append(task.isCompleted() ? "Completed" : "Incomplete")
	                    .append("\n");
	        }

	        prompt.append("""
	                
	                Based only on the information provided above:
	                1. Give a short summary of the user's current productivity.
	                2. Give one useful observation.
	                3. Give one practical suggestion.
	                4. Give one short motivational message.

	                Do not invent information that is not present in the data.
	                """);

	        return prompt.toString();
	    }

}
