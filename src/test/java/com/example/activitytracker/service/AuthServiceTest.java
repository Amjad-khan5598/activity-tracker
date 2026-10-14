package com.example.activitytracker.service;

import com.example.activitytracker.exception.EmailAlreadyExistsException;
import com.example.activitytracker.model.User;
import com.example.activitytracker.repository.UserRepository;
import com.example.activitytracker.DTO.SignupRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authService = new AuthService(userRepository, passwordEncoder);
    }

    @Test
    void shouldRejectEmailThatAlreadyExists() {
        SignupRequest request = new SignupRequest();
        request.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.signup(request)
        );

        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void shouldTranslateDuplicateEmailConstraintViolation() {
        SignupRequest request = new SignupRequest();
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("new@example.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("hashed-password");

        SQLException sqlException =
                new SQLException("Duplicate entry", "23000", 1062);

        ConstraintViolationException constraintException =
                new ConstraintViolationException(
                        "Duplicate email",
                        sqlException,
                        "uk_user_email"
                );

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(new DataIntegrityViolationException(
                        "Duplicate email", constraintException));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.signup(request)
        );
    }

    @Test
    void shouldRethrowUnrelatedIntegrityViolation() {
        SignupRequest request = new SignupRequest();
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("new@example.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("hashed-password");

        SQLException sqlException =
                new SQLException("Other constraint failure", "23000", 1452);

        ConstraintViolationException constraintException =
                new ConstraintViolationException(
                        "Other constraint",
                        sqlException,
                        "some_other_constraint"
                );

        DataIntegrityViolationException databaseException =
                new DataIntegrityViolationException(
                        "Other integrity failure", constraintException);

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(databaseException);

        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authService.signup(request)
        );

        assertSame(databaseException, thrown);
    }
}
