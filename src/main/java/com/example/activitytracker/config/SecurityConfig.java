package com.example.activitytracker.config;

import org.springframework.context.annotation.Bean;
import com.example.activitytracker.security.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.activitytracker.security.JwtAuthenticationFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {
	
	 private final JwtAuthenticationFilter jwtAuthenticationFilter;
	 private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	 
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint){
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.jwtAuthenticationEntryPoint= jwtAuthenticationEntryPoint;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
	    .csrf(csrf -> csrf.disable())
	    .sessionManagement(session ->
	        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    )
	    .authorizeHttpRequests(auth ->
	        auth.requestMatchers("/api/auth/**").permitAll()
	            .anyRequest().authenticated()
	    )
	    .exceptionHandling(exception ->
	    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
	)
	    .addFilterBefore(
	        jwtAuthenticationFilter,
	        UsernamePasswordAuthenticationFilter.class
	    );

	    return http.build();
	}
}
