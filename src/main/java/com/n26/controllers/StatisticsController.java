package com.n26.controllers;

import com.n26.model.Statistics;
import com.n26.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle statistics requests
 */

@RestController
@RequestMapping(value = "/statistics", produces = "application/json")
public class StatisticsController {

	private StatisticsService statisticsService;

	@Autowired
	public StatisticsController(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Statistics get() {
		return this.statisticsService.get();
	}
}
