package com.urlshortener.restservice.redis.model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@RedisHash("Url")
public class Url implements Serializable {

    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String url;

    public Url(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Url{" + "id='" + id + '\'' + ", url='" + url + '}';
    }
}