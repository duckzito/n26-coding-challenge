package com.n26.repositories;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache interface
 */
public interface TransactionCache {
	void put(Transaction transaction);

	void clear();

	Statistics get();
}
