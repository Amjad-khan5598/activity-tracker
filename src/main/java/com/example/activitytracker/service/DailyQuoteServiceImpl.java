package com.example.activitytracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import org.springframework.stereotype.Service;

import com.example.activitytracker.model.DailyQuote;
import com.example.activitytracker.model.Quote;
import com.example.activitytracker.repository.DailyQuoteRepository;
import com.example.activitytracker.repository.QuoteRepository;

@Service
public class DailyQuoteServiceImpl implements DailyQuoteService{
	 
	private final DailyQuoteRepository dailyQuoteRepository;
	
	private final QuoteRepository quoteRepository;
	
	public DailyQuoteServiceImpl( DailyQuoteRepository dailyQuoteRepository,QuoteRepository quoteRepository) {
		this.dailyQuoteRepository = dailyQuoteRepository;
		this.quoteRepository=quoteRepository;
	}
	
	@Override
	public DailyQuote getDailyQuote() {
	    LocalDate today = LocalDate.now();

	    Optional<DailyQuote> existingQuote =
	            dailyQuoteRepository.findByDate(today);

	    if (existingQuote.isPresent()) {
	        return existingQuote.get();
	    }

	    List<Quote> quotes = quoteRepository.findAll();

	    Random random = new Random();

	    Quote selectedQuote =
	            quotes.get(random.nextInt(quotes.size()));

	    DailyQuote dailyQuote = new DailyQuote();

	    dailyQuote.setDate(today);
	    dailyQuote.setQuote(selectedQuote);

	    dailyQuoteRepository.save(dailyQuote);

	    return dailyQuote;
	}		
}
