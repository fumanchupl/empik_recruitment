package com.recruitment.githubconnector.service;

import com.recruitment.githubconnector.domain.LoginCounter;
import com.recruitment.githubconnector.domain.response.GitHubResponse;
import com.recruitment.githubconnector.domain.response.UserDataResponse;
import com.recruitment.githubconnector.persistence.LoginCounterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GithubConnectorService {

	private final Logger log = LoggerFactory.getLogger(GithubConnectorService.class);
	private final LoginCounterRepository loginCounterRepository;
	private final GithubClient githubClient;

	public GithubConnectorService(LoginCounterRepository loginCounterRepository, GithubClient githubClient) {
		this.loginCounterRepository = loginCounterRepository;
		this.githubClient = githubClient;
	}

	public Mono<UserDataResponse> getUserMetrics(String user) {
		return githubClient.getUserResponse(user)
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
