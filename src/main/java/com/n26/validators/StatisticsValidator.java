package com.n26.validators;

import com.n26.model.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Statistics Validator
 */
@Component
public class StatisticsValidator {

	private long ttl;

	@Autowired
	public StatisticsValidator(@Value("${ttl}") long ttl ) {
		this.ttl = ttl;
	}

	public Boolean isValidStatistics(final Statistics statistic) {
		return statistic.getAge() <= ttl;
	}

	public boolean isValidStatistics(final Long timestamp) {
		final long currentTime = System.currentTimeMillis();
		return (currentTime - timestamp <= ttl);
	}

}
