package com.example.activitytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activitytracker.model.DailyQuote;


public interface DailyQuoteRepository extends JpaRepository<DailyQuote, Long> {

}
