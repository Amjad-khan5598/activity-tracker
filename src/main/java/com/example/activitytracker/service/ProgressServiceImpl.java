package com.example.activitytracker.service;

import org.springframework.stereotype.Service;

import com.example.activitytracker.DTO.ProgressResponseDTO;
import com.example.activitytracker.repository.TaskEntryRepository;

@Service
public class ProgressServiceImpl implements ProgressService{

	private final TaskEntryRepository taskEntryRepository;
	public ProgressServiceImpl(TaskEntryRepository taskEntryRepository) {
		this.taskEntryRepository=taskEntryRepository;
	}
	
	@Override
		public ProgressResponseDTO getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

}
