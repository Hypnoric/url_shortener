package com.urlshortener.restservice;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.urlshortener.restservice.redis.model.Url;
import com.urlshortener.restservice.redis.repository.UrlRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UrlControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UrlRepository urlRepository;

	@Test
	public void testPostValidUrl() throws Exception {

		String stringUrl = "https://test.com";
		String sha256hex = Hashing.sha256()
  			.hashString(stringUrl, StandardCharsets.UTF_8)
  			.toString().substring(0, 10);
		String shortened = "http://localhost/" + sha256hex;

		Map<String,Object> body = new HashMap<>();
    	body.put("url",stringUrl);

		this.mockMvc.perform( MockMvcRequestBuilders
				.post("/url")
				.content(asJsonString(body))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(shortened));

		// The same input returns the same shortened url
		this.mockMvc.perform( MockMvcRequestBuilders
				.post("/url")
				.content(asJsonString(body))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(shortened));

		// Different url returns another shortened url
		stringUrl = "https://test2.com";
		sha256hex = Hashing.sha256()
  			.hashString(stringUrl, StandardCharsets.UTF_8)
  			.toString().substring(0, 10);
		String shortened2 = "http://localhost/" + sha256hex;

		body = new HashMap<>();
    	body.put("url",stringUrl);

		this.mockMvc.perform( MockMvcRequestBuilders
				.post("/url")
				.content(asJsonString(body))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(shortened2));

			assertNotEquals(shortened, shortened2);
	}

	@Test
	public void testPostInvalidUrl() throws Exception {

		String stringUrl = "invalid url";

		Map<String,Object> body = new HashMap<>();
    	body.put("url",stringUrl);

		this.mockMvc.perform( MockMvcRequestBuilders
				.post("/url")
				.content(asJsonString(body))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetValidUrl() throws Exception {

		String id = "9876543210";
		String stringUrl = "https://test3.com";

		Mockito.when(urlRepository.findById(id)).thenReturn(Optional.of(new Url(id, stringUrl)));
		this.mockMvc.perform(get("/{url}", id))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(stringUrl));
	}

	@Test
	public void testGetInvalidUrl() throws Exception {

		this.mockMvc.perform(get("/{url}", "foo"))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void testGetNonexistentUrl() throws Exception {

		this.mockMvc.perform(get("/{url}", "1234567890"))
				.andDo(print()).andExpect(status().isNotFound());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
