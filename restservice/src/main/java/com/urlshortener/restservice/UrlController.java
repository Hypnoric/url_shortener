package com.urlshortener.restservice;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.hash.Hashing;
import com.urlshortener.restservice.exceptions.InvalidUrlException;
import com.urlshortener.restservice.exceptions.UrlNotFoundException;
import com.urlshortener.restservice.models.urlRequestBody;
import com.urlshortener.restservice.redis.model.Url;
import com.urlshortener.restservice.redis.repository.UrlRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class UrlController {

	// Regex to validate shortened url received by the GET endpoint, 10 hexadecimal characters in lowercase
	private static final Pattern idRegex = Pattern.compile("^[0-9a-f]{10}$");

	@Autowired
    private UrlRepository urlRepository;

	@PostMapping("/url")
	@Operation(
		summary = "Shorten an url",
		responses = { @ApiResponse(responseCode = "200", description = "URL successfully shortened", content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "400", description = "Invalid or missing url")}
	)
	@ResponseStatus(HttpStatus.OK)
	public String shortenUrl(
		@Valid @RequestBody urlRequestBody input,
		Errors errors
	) {
		if (errors.hasErrors()) {
			throw new InvalidUrlException("Invalid or missing url");
		}

		// Using sha256 hashing to get a value for the shortened url that will be constant for the same input
		String sha256hex = Hashing.sha256()
  			.hashString(input.getUrl(), StandardCharsets.UTF_8)
  			.toString().substring(0, 10);

		// Save to redis
		Url url = new Url(sha256hex, input.getUrl());
		urlRepository.save(url);

		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/" + sha256hex;
	}


	@GetMapping("/{id}")
	@Operation(
		summary = "Return the full url for a specified shortened url id",
		responses = { @ApiResponse(responseCode = "200", description = "Full URL is found", content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "400", description = "The shortened url does not match the expected format"),
		@ApiResponse(responseCode = "404", description = "Url not found in redis")}
	)
	@ResponseStatus(HttpStatus.OK)
	public String getFullUrl(@PathVariable String id) {

		// Validate with regex InvalidUrlException if error
		Matcher m = idRegex.matcher(id);
		if (!m.matches()) {
			throw new InvalidUrlException("The shortened url does not match the expected format");
		} 

		// Try to get url from redis, throw error if not found
		try {
			Url url = urlRepository.findById(id).get();
			return url.getUrl();
		} catch (Exception e) {
			throw new UrlNotFoundException("Url not found in redis");
		}
	}
}