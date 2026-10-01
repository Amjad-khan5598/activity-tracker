package com.example.activitytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ActivitytrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivitytrackerApplication.class, args);
	}

}
