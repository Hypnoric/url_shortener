package com.urlshortener.restservice.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Exception class for handling invalid url field.
 */
public class InvalidUrlException extends RuntimeException {

	// Http Status that the error response should use based on this exception
	@Getter
	private static final HttpStatus status = HttpStatus.BAD_REQUEST;

	/**
	 * Error for invalid url field.
	 *
	 * @param message Error message.
	 */
	public InvalidUrlException(String message) {
		super(message);
	}
}
