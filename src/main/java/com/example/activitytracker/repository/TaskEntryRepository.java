package com.example.activitytracker.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activitytracker.model.TaskEntry;
import com.example.activitytracker.model.User;

public interface TaskEntryRepository
        extends JpaRepository<TaskEntry, Long> {

    
    Page<TaskEntry> findByUser(User user, Pageable pageable);

   
    List<TaskEntry> findByUser(User user);
}
