package com.n26.repositories;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache interface
 */
public interface TransactionCache {
	void update(int position, Statistics statistics, Transaction transaction);
	void insert(int position, Transaction transaction);
	void clear();
}
