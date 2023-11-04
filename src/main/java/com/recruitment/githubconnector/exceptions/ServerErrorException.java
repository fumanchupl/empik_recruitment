package com.recruitment.githubconnector.exceptions;

public class ServerErrorException extends RuntimeException {
	public ServerErrorException(Throwable ex) {
		super(ex);
	}
}
