package com.example.activitytracker.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activitytracker.model.DailyQuote;


public interface DailyQuoteRepository extends JpaRepository<DailyQuote, Long> {
	Optional<DailyQuote> findByDate(LocalDate date);
}
