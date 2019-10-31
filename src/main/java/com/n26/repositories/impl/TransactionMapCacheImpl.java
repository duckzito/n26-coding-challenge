package com.n26.repositories.impl;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repositories.TransactionCache;
import com.n26.utils.StatisticsMapsUtils;
import com.n26.validators.StatisticsValidator;
import com.n26.validators.TransactionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.n26.utils.StatisticsMapsUtils.toStatisticMap;
import static com.n26.utils.StatisticsMapsUtils.updateExisting;

/**
 * Cache implementation based on {@link ConcurrentHashMap}
 */

@Slf4j
@Repository
public class TransactionMapCacheImpl implements TransactionCache {

	private final Object lockObj = new Object();

	private int size = 60;

	//package visibility for testing purposes
	ConcurrentHashMap<Integer, Map<String, Object>> concurrentHashMap;

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
			concurrentHashMap.put(i, new HashMap<>());
		}
	}


	/**
	 * Put Method
	 * Used for storing a new {@Link Transaction}
	 * @param transaction
	 */
	@Override
	public void put(Transaction transaction) {
		transactionValidator.validateTransaction(transaction);

		final int position = this.getPositionWindow(transaction);
		final Map<String, Object> statistics = concurrentHashMap.get(position);

		synchronized (lockObj) {
			if (!statistics.isEmpty() && validator.isValidStatistics((long) statistics.get("timestamp"))) {
				concurrentHashMap.put(position, updateExisting(statistics, transaction));
			} else {
				concurrentHashMap.put(position, toStatisticMap(transaction));
			}
		}
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
				.reduce(StatisticsMapsUtils::mergeStatistics)
				.map(Statistics::build)
				.orElseGet(Statistics::new);
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

	private int getPositionWindow(final Transaction transaction) {
		final long currentTime = System.currentTimeMillis();
		final int position = (int) ((currentTime - transaction.getTimestampInMillis()) / 1000);
		return position % size;
	}

	private boolean isValid(Map<String, Object> statistics) {
		if (statistics.isEmpty()) return false;
		synchronized (lockObj) {
			Long timestamp = (Long) statistics.get("timestamp");
			return validator.isValidStatistics(timestamp);
		}
	}
}
