package com.example.activitytracker.service;

import java.util.Optional;
import java.sql.SQLException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.activitytracker.repository.UserRepository;
import com.example.activitytracker.model.User;
import com.example.activitytracker.DTO.LoginRequest;
import com.example.activitytracker.DTO.SignupRequest;
import com.example.activitytracker.DTO.UserDTO;
import com.example.activitytracker.exception.EmailAlreadyExistsException;
import com.example.activitytracker.exception.InvalidCredentialsException;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User signup(SignupRequest request) {
		Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
		if (userOptional.isPresent()) {
			throw new EmailAlreadyExistsException("User already exists");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole("USER");
		
		try {
		    return userRepository.saveAndFlush(user);
		} catch (DataIntegrityViolationException ex) {
		    if (isEmailDuplicate(ex)) {
		        throw new EmailAlreadyExistsException("User already exists");
		    }
		    throw ex;
		}
	}
	
	private boolean isEmailDuplicate(DataIntegrityViolationException ex) {
	    Throwable cause = ex;

	    while (cause != null) {
	        if (cause instanceof ConstraintViolationException cve) {
	            SQLException sqlException = cve.getSQLException();

	            if (sqlException != null
	                    && sqlException.getErrorCode() == 1062
	                    && cve.getConstraintName() != null
	                    && cve.getConstraintName().contains("uk_user_email")) {
	                return true;
	            }
	        }

	        cause = cause.getCause();
	    }

	    return false;
	}

	public User login(LoginRequest request) {
		Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
		if (userOptional.isEmpty()) {
			throw new InvalidCredentialsException("Invalid email or password");
		}
		User user = userOptional.get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid email or password");
		}
		return user;
	}
}