package com.recruitment.githubconnector.exception;

import com.recruitment.githubconnector.domain.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse userNotFound(UserNotFoundException ex) {
		return new ErrorResponse("User not found", "No user with given login found");
	}

	@ExceptionHandler({ServerErrorException.class, Throwable.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse unhandled(Throwable ex) {
		return new ErrorResponse("Server error", "Please contact support");
	}
}
