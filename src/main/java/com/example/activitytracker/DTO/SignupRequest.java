package com.example.activitytracker.DTO;

import lombok.Data;

@Data
public class SignupRequest {
	private String name;
	private String password;
	private String email;

}
