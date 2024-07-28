package com.urlshortener.restservice.models;

import lombok.Getter;

import org.springframework.http.HttpStatus;


/**
 * Error response model.
 */
public class ErrorResponse {

	@Getter
	private String message;

	@Getter
	private HttpStatus status;

	/**
	 * Error Response constructor with parameters.
	 *
	 * @param status  HTTP status for the error response.
	 * @param message Error message.
	 */
	public ErrorResponse(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * Default constructor required for deserialization.
	 */
	public ErrorResponse() {
	}

	// setter used to deserialize from JSON strings where status: <int>
	public void setStatus(int status) {
		this.status = HttpStatus.valueOf(status);
	}

}
