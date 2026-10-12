
package com.example.activitytracker.service;

import com.example.activitytracker.DTO.AICoachResponseDTO;
import com.example.activitytracker.DTO.ProgressResponseDTO;
import com.example.activitytracker.exception.AIResponseException;
import com.example.activitytracker.model.User;
import com.example.activitytracker.repository.TaskEntryRepository;
import com.example.activitytracker.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AICoachServiceImplTest {

    @Mock
    private GeminiClientService geminiClientService;

    @Mock
    private ProgressService progressService;

    @Mock
    private TaskEntryRepository taskEntryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AICoachServiceImpl aiCoachService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowAIResponseExceptionWhenResponseIsInvalid() throws Exception {

        // Simulate an authenticated user
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "test@example.com", null
                )
        );

        // Prepare the user
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(taskEntryRepository.findByUser(user))
                .thenReturn(List.of());

        when(progressService.getProgress())
                .thenReturn(new ProgressResponseDTO());

        // Simulate Gemini returning text
        when(geminiClientService.generateContent(anyString()))
                .thenReturn("invalid JSON");

        // Simulate Jackson failing to parse that text
        when(objectMapper.readValue(
                anyString(),
                eq(AICoachResponseDTO.class)
        )).thenThrow(new JsonProcessingException("Invalid JSON") {});

        // Verify the service converts the parsing error
        // into AIResponseException
        assertThrows(
                AIResponseException.class,
                () -> aiCoachService.getCoaching()
        );
    }
}
