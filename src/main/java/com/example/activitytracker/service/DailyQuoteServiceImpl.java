package com.example.activitytracker.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.activitytracker.model.DailyQuote;
import com.example.activitytracker.repository.DailyQuoteRepository;

@Service
public class DailyQuoteServiceImpl implements DailyQuoteService {

    private final DailyQuoteRepository dailyQuoteRepository;

    public DailyQuoteServiceImpl(DailyQuoteRepository dailyQuoteRepository) {
        this.dailyQuoteRepository = dailyQuoteRepository;
    }

    @Override
    public DailyQuote getDailyQuote() {

        LocalDate today = LocalDate.now();

        Optional<DailyQuote> existingQuote =
                dailyQuoteRepository.findByDate(today);

        if (existingQuote.isPresent()) {
            return existingQuote.get();
        }

        throw new RuntimeException("Today's quote has not been generated yet");
    }
}