package com.example.activitytracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activitytracker.model.DailyQuote;
import com.example.activitytracker.service.DailyQuoteService;

@RestController
@RequestMapping("/api/daily-quote")
public class DailyQuoteController {

    private final DailyQuoteService dailyQuoteService;

    public DailyQuoteController(DailyQuoteService dailyQuoteService) {
        this.dailyQuoteService = dailyQuoteService;
    }

    @GetMapping
    public DailyQuote getDailyQuote() {
        return dailyQuoteService.getDailyQuote();
    }
}