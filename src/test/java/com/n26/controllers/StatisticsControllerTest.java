package com.n26.controllers;

import com.n26.model.Statistics;
import com.n26.services.StatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
public class StatisticsControllerTest {

	private MockMvc mvc;

	private StatisticsController controller;

	@Mock
	private StatisticsService service;

	@Before
	public void setUp() {
		this.controller = new StatisticsController(this.service);

		this.mvc = MockMvcBuilders.standaloneSetup(this.controller)
				.build();
	}

	@Test
	public void givenAGetThenStatisticsShouldBeReturned() throws Exception {
		when(this.service.get()).thenReturn(new Statistics());
		Statistics statistics = this.controller.get();

		assertEquals(0L, statistics.getCount().longValue());
		verify(this.service, times(1)).get();
	}

}

