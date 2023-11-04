package com.recruitment.githubconnector.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "login_counter")
public class LoginCounter {

	@Id
	private String login;

	@Column(name = "request_count")
	private long requestCount = 0;


	public LoginCounter() {
	}

	public LoginCounter(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(Long requestCount) {
		this.requestCount = requestCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LoginCounter that = (LoginCounter) o;
		return requestCount == that.requestCount && Objects.equals(login, that.login);
	}

	@Override
	public int hashCode() {
		return Objects.hash(login, requestCount);
	}

	@Override
	public String toString() {
		return "UserMetric{" +
				"login='" + login + '\'' +
				", requestCount=" + requestCount +
				'}';
	}
}
