package com.recruitment.githubconnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recruitment.githubconnector.domain.LoginCounter;
import com.recruitment.githubconnector.domain.response.GitHubResponse;
import com.recruitment.githubconnector.repository.LoginCounterRepository;
import com.recruitment.githubconnector.service.GithubClient;
import com.recruitment.githubconnector.service.GithubConnectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GithubConnectorServiceTest {
	private GithubConnectorService service;
	private final GithubClient githubClient = mock(GithubClient.class);
	private final LoginCounterRepository repository = mock(LoginCounterRepository.class);

	@BeforeEach
	void initialize() {
		service = new GithubConnectorService(repository, githubClient);
	}

	@Test
	void TestNoFollowersShouldReturnZeroCalculations() throws JsonProcessingException {
		GitHubResponse mockResponse = new GitHubResponse(0, "user", "name", "User", "avatarUrl", LocalDate.now(), 0, 0);
		when(githubClient.getUserResponse(eq("user"))).thenReturn(Mono.just(mockResponse));
		StepVerifier.create(service.getUserMetrics("user")).assertNext(response -> {
			assertEquals(0, response.calculations(), 0.001, "Calculations should equal 0 on 0 followers count");
		}).verifyComplete();
	}

	@Test
	void TestRequestCountShouldIncreaseAfterResponse() {
		GitHubResponse mockResponse = new GitHubResponse(0, "login", "name", "User", "avatarUrl", LocalDate.now(), 1, 1);
		when(githubClient.getUserResponse(eq("login"))).thenReturn(Mono.just(mockResponse));
		LoginCounter loginCounterMock = new LoginCounter("login");
		long initialCount = 5L;
		loginCounterMock.setRequestCount(initialCount);
		when(repository.findById(anyString())).thenReturn(Optional.of(loginCounterMock));

		service.getUserMetrics("login").subscribe();

		ArgumentCaptor<LoginCounter> loginCounterCaptor = ArgumentCaptor.forClass(LoginCounter.class);
		verify(repository).save(loginCounterCaptor.capture());

		LoginCounter savedEntity = loginCounterCaptor.getValue();
		assertEquals(loginCounterMock.getLogin(), savedEntity.getLogin(), "Login should match");
		assertEquals(initialCount + 1, savedEntity.getRequestCount(), "Request count should be increased by 1");
	}

	@Test
	void TestResponseDataShouldMatch() {
		GitHubResponse mockResponse = new GitHubResponse(0, "login", "name", "User", "avatarUrl", LocalDate.now(), 1, 1);
		when(githubClient.getUserResponse(eq("login"))).thenReturn(Mono.just(mockResponse));

		StepVerifier.create(service.getUserMetrics("login")).assertNext(resposnse -> {
			assertEquals(mockResponse.login(), resposnse.login(), "Login should match");
			assertEquals(mockResponse.name(), resposnse.name(), "Name should match");
			assertEquals(mockResponse.type(), resposnse.type(), "Type should match");
			assertEquals(mockResponse.avatarUrl(), resposnse.avatarUrl(), "Avatar URL should match");
			assertEquals(mockResponse.createdAt(), resposnse.createdAt(), "Creation date should match");
		});
	}

	@Test
	void TestCalculationShouldMatchTheFormula() {
		//FORMULA: 6 / followerCount * (2 + repoCount)
		long followersCount = 46783;
		long repoCount = 53342;
		GitHubResponse mockResponse = new GitHubResponse(0, "login", "name", "User", "avatarUrl", LocalDate.now(), followersCount, repoCount);
		when(githubClient.getUserResponse(eq("login"))).thenReturn(Mono.just(mockResponse));

		StepVerifier.create(service.getUserMetrics("login")).assertNext(response -> {
			assertEquals(6 / (double) followersCount * (2 + repoCount), response.calculations(), "Calculations should match the formula");
		});
	}
}
