package com.recruitment.githubconnector.controller;

import com.recruitment.githubconnector.domain.LoginCounter;
import com.recruitment.githubconnector.persistence.LoginCounterRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debug")
@Profile("dev")
public class DebugController {
	private final LoginCounterRepository repository;

	public DebugController(LoginCounterRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/users")
	public List<LoginCounter> getUsers() {
		return repository.findAll();
	}
}
