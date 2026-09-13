package com.example.activitytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activitytracker.model.TaskEntry;

public interface DailyQuoteRepository extends JpaRepository<TaskEntry, Long> {

}
