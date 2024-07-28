package com.urlshortener.restservice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.urlshortener.restservice.exceptions.InvalidUrlException;
import com.urlshortener.restservice.exceptions.UrlNotFoundException;
import com.urlshortener.restservice.models.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Error handler to catch different types of errors.
 */
@RestControllerAdvice
public class ErrorHandler {

	static final String FAILED_AUTHENTICATE = "Failed to authenticate player: ";
	private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

	/**
	 * Get up to 5 elements of the stack trace and log them so the error can be
	 * tracked.
	 *
	 * @param message A customized error message.
	 * @param ex      Exception.
	 */
	private static void logErrorMessage(String message, Exception ex) {
		StringBuilder errorMessage = new StringBuilder(message);
		errorMessage.append(System.getProperty("line.separator"));
		errorMessage.append(ex);
		errorMessage.append(System.getProperty("line.separator"));
		List<StackTraceElement> shortTraceList = Arrays.stream(ex.getStackTrace()).collect(Collectors.toList()).subList(0,
				Math.min(5, ex.getStackTrace().length)
		);
		errorMessage.append(String.format("Source: %n%s%n...",
				shortTraceList.stream().map(StackTraceElement::toString).collect(Collectors.joining("\n\t"))
		));
		log.error("{}", errorMessage);
	}

	/**
	 * Catch generic error exceptions.
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse exception(HttpServletRequest request, Exception ex) {
		logErrorMessage("Some internal error has occurred: ", ex);

		return new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Internal Server Error"
		);
	}

	/**
	 * Catch url not found exceptions.
	 */
	@ExceptionHandler(UrlNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleUserNotFoundException(HttpServletRequest request, Exception ex) {
		logErrorMessage("User Not Found: ", ex);

		return new ErrorResponse(
			UrlNotFoundException.getStatus(),
			ex.getMessage()
		);
	}

	/**
	 * Catch invalid url field exceptions.
	 */
	@ExceptionHandler(InvalidUrlException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMissingMandatoryRequestException(HttpServletRequest request, Exception ex) {
		logErrorMessage("Missing Mandatory Field: ", ex);

		return new ErrorResponse(
			InvalidUrlException.getStatus(),
			ex.getMessage()
		);
	}
}
