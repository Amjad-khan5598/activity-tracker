package com.example.activitytracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activitytracker.DTO.ProgressResponseDTO;
import com.example.activitytracker.service.ProgressService;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
	
		private final ProgressService progressService;
		
		public ProgressController(ProgressService progressService) {
			this.progressService = progressService;
			
		}
		
	@GetMapping
	 public ProgressResponseDTO getProgress() {
        return progressService.getProgress();
    }
		
}
