package com.n26.repositories;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

/**
 * Statistics Cache interface
 */
public interface StatisticsCache {
	Statistics get();
	Statistics get(int position);
}
