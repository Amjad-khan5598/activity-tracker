package com.example.activitytracker.service;

import com.example.activitytracker.DTO.AICoachResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	 private final ObjectMapper objectMapper;

	    public AICoachServiceImpl(Client geminiClient,ProgressService progressService,
	    		TaskEntryRepository taskEntryRepository,
	    		UserRepository userRepository,ObjectMapper objectMapper) {
	        
	    	this.geminiClient = geminiClient;
	        this.progressService=progressService;
	        this.taskEntryRepository=taskEntryRepository;
	        this.userRepository=userRepository;
	        this.objectMapper=objectMapper;	    
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

	        AICoachResponseDTO coachResponse;

	        try {
	            coachResponse = objectMapper.readValue(
	                    response,
	                    AICoachResponseDTO.class
	            );
	        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
	            throw new RuntimeException("Failed to parse Gemini response", e);
	        }


	        return coachResponse;
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
	                
	              Based only on the information provided above, return a valid JSON object
        with exactly these four string fields:

        {
          "summary": "A short summary of the user's productivity",
          "feedback": "One useful observation about their progress",
          "suggestion": "One practical action they can take",
          "motivation": "One short motivational message"
        }

        Rules:
        - Return only the JSON object.
        - Do not include Markdown code fences.
        - All four fields must be strings.
        - Do not invent information that is not present in the data.
        """);

	        return prompt.toString();
	    }

}
