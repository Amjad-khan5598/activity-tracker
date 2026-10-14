package com.example.activitytracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
	@NotBlank
	private String name;

	@NotBlank
	@Email
	private String email;

	@Size(min = 8, max = 72,
		      message = "Password must be between 8 and 72 characters")
	private String password;

}
