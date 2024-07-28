package com.urlshortener.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerTests {

	@Autowired
	private MockMvc mockMvc;
/*
	@Test
	public void invalidPhoneShouldReturnError() throws Exception {

		this.mockMvc.perform(get("/v1/phone-numbers")).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void validPhoneShouldReturnInfo() throws Exception {

		this.mockMvc.perform(get("/v1/phone-numbers").param("phoneNumber", "12125690123"))
				.andDo(print()).andExpect(status().isOk());
	}
*/
}
