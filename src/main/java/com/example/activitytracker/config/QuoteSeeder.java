package com.example.activitytracker.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.activitytracker.model.Quote;
import com.example.activitytracker.repository.QuoteRepository;

@Component
public class QuoteSeeder implements CommandLineRunner {

    private final QuoteRepository quoteRepository;

    public QuoteSeeder(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public void run(String... args) {

        if (quoteRepository.count() == 0) {

        	List<Quote> quotes = List.of(
        		    createQuote("Stay hungry, stay foolish.", "Steve Jobs"),
        		    createQuote("The journey of a thousand miles begins with one step.", "Lao Tzu"),
        		    createQuote("Success is the sum of small efforts, repeated day in and day out.", "Robert Collier"),
        		    createQuote("It always seems impossible until it's done.", "Nelson Mandela"),
        		    createQuote("The secret of getting ahead is getting started.", "Mark Twain"),
        		    createQuote("Well done is better than well said.", "Benjamin Franklin"),
        		    createQuote("Great things are done by a series of small things brought together.", "Vincent van Gogh"),
        		    createQuote("Believe you can and you're halfway there.", "Theodore Roosevelt"),
        		    createQuote("Don't watch the clock; do what it does. Keep going.", "Sam Levenson"),
        		    createQuote("The future depends on what you do today.", "Mahatma Gandhi"),
        		    createQuote("Success is not final; failure is not fatal.", "Winston Churchill"),
        		    createQuote("Action is the foundational key to all success.", "Pablo Picasso"),
        		    createQuote("You miss 100% of the shots you don't take.", "Wayne Gretzky"),
        		    createQuote("The only way to do great work is to love what you do.", "Steve Jobs"),
        		    createQuote("Start where you are. Use what you have. Do what you can.", "Arthur Ashe"),
        		    createQuote("Small progress is still progress.", "Unknown"),
        		    createQuote("Discipline is the bridge between goals and accomplishment.", "Jim Rohn"),
        		    createQuote("Dream big. Start small. Act now.", "Robin Sharma"),
        		    createQuote("Don't limit your challenges. Challenge your limits.", "Jerry Dunn"),
        		    createQuote("A little progress each day adds up to big results.", "Unknown"),
        		    createQuote("Success usually comes to those who are too busy to be looking for it.", "Henry David Thoreau"),
        		    createQuote("The harder I work, the luckier I get.", "Samuel Goldwyn"),
        		    createQuote("Do something today that your future self will thank you for.", "Unknown"),
        		    createQuote("Great things never come from comfort zones.", "Unknown"),
        		    createQuote("Focus on being productive instead of busy.", "Tim Ferriss"),
        		    createQuote("You don't have to be great to start, but you have to start to be great.", "Zig Ziglar"),
        		    createQuote("Consistency is the key to success.", "Unknown"),
        		    createQuote("What you do every day matters more than what you do once in a while.", "Gretchen Rubin"),
        		    createQuote("Success is built one day at a time.", "Unknown"),
        		    createQuote("Keep going. Everything you need will come to you at the perfect time.", "Unknown")
        		);

            quoteRepository.saveAll(quotes);

            System.out.println("Quotes seeded successfully.");
        }
    }

    private Quote createQuote(String quoteText, String author) {
        Quote quote = new Quote();
        quote.setQuote(quoteText);
        quote.setAuthor(author);
        return quote;
    }
}
