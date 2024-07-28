package com.urlshortener.restservice;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.hash.Hashing;
import com.urlshortener.restservice.exceptions.InvalidUrlException;
import com.urlshortener.restservice.models.urlRequestBody;

import jakarta.validation.Valid;

@RestController
public class UrlController {

	private static final Pattern idRegex = Pattern.compile("^[0-9a-f]{10}$"); //maybe not needed

	@PostMapping("/shorten")
	@ResponseStatus(HttpStatus.OK)
	public String shorten(
		@Valid @RequestBody urlRequestBody input,
		Errors errors
	) {
		if (errors.hasErrors()) {
			throw new InvalidUrlException("Invalid or missing url");
		}
		String sha256hex = Hashing.sha256()
  			.hashString(input.getUrl(), StandardCharsets.UTF_8)
  			.toString().substring(0, 10);
		//save to redis
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/" + sha256hex;
	}


	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public String fullUrl(
		@RequestParam(value = "url") String stringUrl) {

		//try to get from redis throw error if not there  

		return "";
	}
}