package com.urlshortener.restservice.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.urlshortener.restservice.redis.model.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, String> {}