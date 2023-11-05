package com.recruitment.githubconnector.controller;

import com.recruitment.githubconnector.domain.response.UserDataResponse;
import com.recruitment.githubconnector.service.GithubConnectorService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

	private final GithubConnectorService connectorService;

	public UserController(GithubConnectorService connectorService) {
		this.connectorService = connectorService;
	}

	@GetMapping("/{user}")
	@ResponseBody
	public Mono<UserDataResponse> getUserMetrics(@PathVariable("user") String user) {
		return connectorService.getUserMetrics(user);
	}
}
