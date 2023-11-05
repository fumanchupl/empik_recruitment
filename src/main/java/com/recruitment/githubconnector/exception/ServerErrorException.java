package com.recruitment.githubconnector.exception;

public class ServerErrorException extends RuntimeException {
	public ServerErrorException(Throwable ex) {
		super(ex);
	}
}
