package com.urlshortener.restservice;

import java.nio.charset.StandardCharsets;
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

import jakarta.validation.Valid;

@RestController
public class UrlController {

	private static final Pattern idRegex = Pattern.compile("^[0-9a-f]{10}$");

	@Autowired
    private UrlRepository urlRepository;

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
		Url url = new Url(sha256hex, input.getUrl());
		urlRepository.save(url);

		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/" + sha256hex;
	}


	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String fullUrl(@PathVariable String id) {

		//validate with regex InvalidUrlException if error

		//try to get url from redis, throw error if not found
		try {
			Url url = urlRepository.findById(id).get();
			return url.getUrl();
		} catch (Exception e) {
			throw new UrlNotFoundException("Url not found in redis");
		}
	}
}