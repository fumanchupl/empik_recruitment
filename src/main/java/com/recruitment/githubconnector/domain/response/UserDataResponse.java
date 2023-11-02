package com.recruitment.githubconnector.domain.response;

import java.time.LocalDate;

public record UserDataResponse(long id, String login, String name, String type, String avatarUrl,
															 LocalDate createdAt, double calculations) {
	public UserDataResponse(GitHubResponse gitHubResponse, double calculation) {
		this(gitHubResponse.id(), gitHubResponse.login(), gitHubResponse.name(), gitHubResponse.type(),
				gitHubResponse.avatarUrl(), gitHubResponse.createdAt(), calculation);
	}
}
