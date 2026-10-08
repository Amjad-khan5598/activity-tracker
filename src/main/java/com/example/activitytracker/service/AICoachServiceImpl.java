package com.example.activitytracker.service;

import com.example.activitytracker.DTO.AICoachResponseDTO;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AICoachServiceImpl implements AICoachService{

	@Override
	public AICoachResponseDTO getCoaching() {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    System.out.println("AI Coach requested by: " + email);

	    return new AICoachResponseDTO();
	}
	

}
