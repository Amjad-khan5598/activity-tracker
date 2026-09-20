package com.example.activitytracker.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.activitytracker.repository.UserRepository;
import com.example.activitytracker.model.User;
import com.example.activitytracker.DTO.LoginRequest;
import com.example.activitytracker.DTO.SignupRequest;
import com.example.activitytracker.DTO.UserDTO;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(SignupRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        
        return userRepository.save(user);
    }
    
    public UserDTO login(LoginRequest request) {
    	Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
    	if (userOptional.isEmpty()) {
    	    throw new RuntimeException("Invalid email or password");
    	    }
    	User user = userOptional.get();
    	if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    	    throw new RuntimeException("Invalid email or password");
    	}
    	return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}