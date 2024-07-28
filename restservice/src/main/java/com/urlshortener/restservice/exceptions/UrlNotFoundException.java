package com.urlshortener.restservice.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Exception class for handling url not found in redis.
 */
public class UrlNotFoundException extends RuntimeException {

	// Http Status that the error response should use based on this exception
	@Getter
	private static final HttpStatus status = HttpStatus.NOT_FOUND;

	/**
	 * Error for url not found in redis.
	 *
	 * @param message Error message.
	 */
	public UrlNotFoundException(String message) {
		super(message);
	}
}
