package com.recruitment.githubconnector.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubResponse(long id, String login, String name, String type,
														 @JsonProperty("avatar_url") String avatarUrl,
														 @JsonProperty("created_at") LocalDate createdAt,
														 @JsonProperty("followers") long followersCount,
														 @JsonProperty("public_repos") long publicReposCount) {

}
