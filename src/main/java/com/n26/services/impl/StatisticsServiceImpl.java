package com.n26.services.impl;

import com.n26.model.Statistics;
import com.n26.repositories.StatisticsCache;
import com.n26.repositories.TransactionCache;
import com.n26.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Statistic Service to handle get Statistics
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private StatisticsCache transactionMapCache;

	@Override
	public Statistics get() {
		return this.transactionMapCache.get();
	}
}
