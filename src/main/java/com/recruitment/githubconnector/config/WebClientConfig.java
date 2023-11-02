package com.recruitment.githubconnector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean("gitHubClient")
	public WebClient gitHubClient() {
		//TODO externalize props
		return WebClient.builder().baseUrl("https://api.github.com/").build();
	}
}
