package com.n26.repositories.impl;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repositories.StatisticsCache;
import com.n26.repositories.TransactionCache;
import com.n26.validators.StatisticsValidator;
import com.n26.validators.TransactionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Cache implementation based on {@link ConcurrentHashMap}
 */

@Slf4j
@Repository
public class TransactionMapCacheImpl implements TransactionCache, StatisticsCache {

	private final Object lockObj = new Object();

	private int size = 60;

	//package visibility for testing purposes
	ConcurrentHashMap<Integer, Statistics> concurrentHashMap;

	@Autowired
	private StatisticsValidator validator;
	@Autowired
	private TransactionValidator transactionValidator;

	//package visibility for testing purposes
	@PostConstruct
	void initialize() {
		log.info("Initializing cache");
		concurrentHashMap = new ConcurrentHashMap(size);
		for (int i = 0; i < size; i++) {
			concurrentHashMap.put(i, new Statistics());
		}
	}

	/**
	 * Update Method
	 * Used for updating a {@Link Statistics} with a {@Link Transaction}
	 * @param position
	 * @param transaction
	 */
	@Override
	public void update(int position, final Statistics statistics, Transaction transaction) {
		//final Statistics statistics = concurrentHashMap.get(position);
		concurrentHashMap.put(position, Statistics.updateExisting(statistics, transaction));
	}

	/**
	 * Put Method
	 * Used for storing a new {@Link Transaction}
	 * @param position
	 * @param transaction
	 */
	@Override
	public void insert(int position, Transaction transaction) {
		concurrentHashMap.put(position, Statistics.buildFrom(transaction));
	}


	/**
	 * Get Statistic
	 *
	 * @return {@Link Statistics}
	 */
	@Override
	public Statistics get() {
		log.info("Calculating statistics");
		return concurrentHashMap.values().parallelStream()
				.filter(this::isValid)
				.reduce(Statistics::mergeStatistics)
				.map( s -> new Statistics(s.getSum(), s.getAvg(), s.getMax(), s.getMin(), s.getCount()))
				.orElseGet(Statistics::new);
	}

	@Override
	public Statistics get(int position) {
		return concurrentHashMap.get(position);
	}


	/**
	 * Clear Transaction repository
	 */
	@Override
	public void clear() {
		concurrentHashMap.clear();
		initialize();
		log.info("Cache cleared");
	}


	private boolean isValid(Statistics statistics) {
		synchronized (lockObj) {
			Long timestamp = statistics.getTimestampInMillis();
			return validator.isValidStatistics(timestamp) && statistics.getSum().compareTo(BigDecimal.ZERO) != 0;
		}
	}
}
