package com.example.activitytracker.service;

import com.example.activitytracker.DTO.TaskRequestDTO;
import com.example.activitytracker.exception.TaskAccessDeniedException;
import com.example.activitytracker.exception.TaskNotFoundException;
import com.example.activitytracker.model.TaskEntry;
import com.example.activitytracker.model.User;
import com.example.activitytracker.repository.TaskEntryRepository;
import com.example.activitytracker.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskEntryServiceImplTest {

    private TaskEntryRepository taskRepository;
    private UserRepository userRepository;
    private TaskEntryServiceImpl taskService;

    private User userA;
    private User userB;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskEntryRepository.class);
        userRepository = mock(UserRepository.class);

        taskService = new TaskEntryServiceImpl(
                taskRepository,
                userRepository
        );

        userA = new User();
        userA.setId(1L);
        userA.setEmail("userA@example.com");

        userB = new User();
        userB.setId(2L);
        userB.setEmail("userB@example.com");

        // By default, authenticate as User B.
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "userB@example.com", null
                )
        );

        when(userRepository.findByEmail("userB@example.com"))
                .thenReturn(Optional.of(userB));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldRejectReadingAnotherUsersTask() {
        TaskEntry task = createTaskOwnedBy(userA);

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        assertThrows(
                TaskAccessDeniedException.class,
                () -> taskService.getTaskById(10L)
        );
    }

    @Test
    void shouldRejectUpdatingAnotherUsersTask() {
        TaskEntry task = createTaskOwnedBy(userA);

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        TaskRequestDTO request = new TaskRequestDTO();
        request.setDate(LocalDate.now());
        request.setTaskDescription("Unauthorized update");
        request.setCompleted(true);

        assertThrows(
                TaskAccessDeniedException.class,
                () -> taskService.update(10L, request)
        );

        verify(taskRepository, never())
                .save(any(TaskEntry.class));
    }

    @Test
    void shouldRejectDeletingAnotherUsersTask() {
        TaskEntry task = createTaskOwnedBy(userA);

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        assertThrows(
                TaskAccessDeniedException.class,
                () -> taskService.delete(10L)
        );

        verify(taskRepository, never())
                .delete(any(TaskEntry.class));
    }

    @Test
    void shouldThrowNotFoundWhenTaskDoesNotExist() {
        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(999L)
        );
    }

    @Test
    void shouldAllowUpdatingOwnTask() {
        authenticateAsUserA();

        TaskEntry task = createTaskOwnedBy(userA);

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(TaskEntry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate newDate = LocalDate.of(2026, 10, 14);

        TaskRequestDTO request = new TaskRequestDTO();
        request.setDate(newDate);
        request.setTaskDescription("Updated my own task");
        request.setCompleted(true);

        taskService.update(10L, request);

        assertEquals(newDate, task.getDate());
        assertEquals("Updated my own task", task.getTaskDescription());
        assertTrue(task.isCompleted());

        verify(taskRepository).save(task);
    }

    @Test
    void shouldAllowDeletingOwnTask() {
        authenticateAsUserA();

        TaskEntry task = createTaskOwnedBy(userA);

        when(taskRepository.findById(10L))
                .thenReturn(Optional.of(task));

        taskService.delete(10L);

        verify(taskRepository).delete(task);
    }

    private void authenticateAsUserA() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "userA@example.com", null
                )
        );

        when(userRepository.findByEmail("userA@example.com"))
                .thenReturn(Optional.of(userA));
    }

    private TaskEntry createTaskOwnedBy(User owner) {
        TaskEntry task = new TaskEntry();
        task.setId(10L);
        task.setDate(LocalDate.now());
        task.setTaskDescription("Private task");
        task.setCompleted(false);
        task.setUser(owner);

        return task;
    }
}
