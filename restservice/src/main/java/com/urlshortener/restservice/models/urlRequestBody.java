package com.urlshortener.restservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

/**
 * Input model for shorten endpoint.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class urlRequestBody {
	@NotEmpty(message = "url is required, cannot be null/empty.")
	@URL(message = "must be a valid url")
	private String url;
}