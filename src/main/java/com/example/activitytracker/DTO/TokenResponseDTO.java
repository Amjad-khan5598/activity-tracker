package com.example.activitytracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDTO {

	private UserDTO userDTO;
	private String token;
	
}
