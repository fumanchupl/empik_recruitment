package com.recruitment.githubconnector.service;

import com.recruitment.githubconnector.domain.response.GitHubResponse;
import com.recruitment.githubconnector.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GithubClient {

	private final Logger log = LoggerFactory.getLogger(GithubClient.class);
	private final WebClient gitHubClient;

	public GithubClient(@Qualifier("gitHubClient") WebClient gitHubClient) {
		this.gitHubClient = gitHubClient;
	}

	public Mono<GitHubResponse> getUserResponse(String user) {
		return gitHubClient.get().uri("/users/" + user)
				//NOTE(Author) media type recommended by GitHub (https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user)
				.header("Accept", "application/vnd.github+json")
				.exchangeToMono(this::processResponse)
				.doOnNext(body -> log.debug("response body: {}", body));
	}

	private Mono<GitHubResponse> processResponse(ClientResponse response) {
		if (response.statusCode().value() == 404) {
			return Mono.error(new UserNotFoundException());
		}
		return response.bodyToMono(GitHubResponse.class);
	}
}
