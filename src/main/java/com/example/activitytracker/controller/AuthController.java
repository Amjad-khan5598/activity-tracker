package com.example.activitytracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.activitytracker.DTO.SignupRequest;
import com.example.activitytracker.DTO.UserDTO;
import com.example.activitytracker.model.User;
import com.example.activitytracker.service.AuthService;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
	 private final AuthService authService;
	 public AuthController(AuthService authService) {
		 this.authService=authService;
	 }
	 @PostMapping("/signup")
		 public ResponseEntity<UserDTO> signup(@RequestBody SignupRequest request){
		 User user = authService.signup(request);
		 UserDTO dto = new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
		 return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	 }
	 
}
