package com.example.activitytracker.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.activitytracker.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}