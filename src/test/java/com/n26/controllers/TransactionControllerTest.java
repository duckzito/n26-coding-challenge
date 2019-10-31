package com.n26.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.model.Transaction;
import com.n26.services.TransactionService;
import com.n26.services.impl.TransactionServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionControllerTest {

	private MockMvc mvc;

	private TransactionController controller;

	@Mock
	private TransactionServiceImpl service;

	private JacksonTester<Transaction> jsonTransaction;

	@Before
	public void setUp() {
		this.controller = new TransactionController(this.service);

		JacksonTester.initFields(this, new ObjectMapper());

		this.mvc = MockMvcBuilders.standaloneSetup(this.controller)
				.build();
	}

	@Test
	public void givenATransactionWhenAddedThenACreatedStatusShouldBeReturned() throws Exception {

		final Transaction transaction = new Transaction(new BigDecimal("300.00"), LocalDateTime.now());

		MockHttpServletResponse response = this.mvc.perform(
				post("/transactions")
						.contentType(MediaType.APPLICATION_JSON).content(this.jsonTransaction.write(new Transaction()).getJson()))
				.andReturn().getResponse();

		verify(this.service, times(1)).add(any(Transaction.class));
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}


	@Test
	public void givenADeleteWhenDeleteIsCallThenNoContentStatusShouldBeReturned() throws Exception {

		MockHttpServletResponse response = this.mvc.perform(
				delete("/transactions"))
				.andReturn().getResponse();

		verify(this.service, times(1)).delete();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

	}
}

