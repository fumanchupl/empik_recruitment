package com.recruitment.githubconnector.service;

import com.recruitment.githubconnector.domain.LoginCounter;
import com.recruitment.githubconnector.domain.response.GitHubResponse;
import com.recruitment.githubconnector.domain.response.UserDataResponse;
import com.recruitment.githubconnector.repository.LoginCounterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GithubConnectorService {

	private final Logger log = LoggerFactory.getLogger(GithubConnectorService.class);
	private final WebClient gitHubClient;
	private final LoginCounterRepository loginCounterRepository;

	public GithubConnectorService(@Qualifier("gitHubClient") WebClient gitHubClient, LoginCounterRepository loginCounterRepository) {
		this.gitHubClient = gitHubClient;
		this.loginCounterRepository = loginCounterRepository;
	}

	public Mono<UserDataResponse> getUserMetrics(String user) {
		return gitHubClient.get().uri("/users/" + user)
				//NOTE(Author) media type recommended by GitHub (https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user)
				.header("Accept", "application/vnd.github+json")
				.exchangeToMono(response -> response.bodyToMono(GitHubResponse.class))
				.doOnNext(body -> log.debug("response body: {}", body))
				.doOnNext(this::persist)
				.map(gitHubResponse -> new UserDataResponse(gitHubResponse, calculate(gitHubResponse.followersCount(), gitHubResponse.publicReposCount())));
	}

	private double calculate(long followersCount, long publicReposCount) {
		if (followersCount == 0) {
			//NOTE(Author) reasonable defaults, since division by zero is probable
			return 0;
		}
		return 6 / (double) followersCount * (2 + publicReposCount);
	}

	private synchronized LoginCounter persist(GitHubResponse response) {
		String login = response.login();
		LoginCounter loginCounter = loginCounterRepository.findById(login).orElse(new LoginCounter(login));
		loginCounter.setRequestCount(loginCounter.getRequestCount() + 1);
		log.info("Persisting {}", loginCounter);
		return loginCounterRepository.save(loginCounter);
	}
}
