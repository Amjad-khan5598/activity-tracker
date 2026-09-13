package com.example.activitytracker.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class TaskEntry {
@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
private LocalDate date;
private String taskDescription;
private boolean completed;
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
}
