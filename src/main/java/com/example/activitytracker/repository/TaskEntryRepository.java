package com.example.activitytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activitytracker.model.DailyQuote;

public interface TaskEntryRepository extends JpaRepository<DailyQuote, Long> {

}
