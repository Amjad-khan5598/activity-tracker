package com.example.activitytracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activitytracker.DTO.AICoachResponseDTO;
import com.example.activitytracker.service.AICoachService;

@RestController
@RequestMapping("/api/ai-coach")
public class AICoachController {
	private final AICoachService aiCoachService;
	
	public AICoachController(AICoachService aiCoachService) {
		this.aiCoachService=aiCoachService;
	}
	
	@GetMapping
	public AICoachResponseDTO getCoaching() {
		return aiCoachService.getCoaching();
	}
}
