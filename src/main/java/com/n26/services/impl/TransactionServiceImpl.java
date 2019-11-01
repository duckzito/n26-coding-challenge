package com.n26.services.impl;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repositories.StatisticsCache;
import com.n26.repositories.TransactionCache;
import com.n26.services.TransactionService;
import com.n26.validators.StatisticsValidator;
import com.n26.validators.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Transaction Service Implementation to handle add and delete transactions.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	private final Object lockObj = new Object();

	@Autowired
	private TransactionCache transactionCache;
	@Autowired
	private StatisticsCache statisticsCache;

	@Autowired
	private TransactionValidator transactionValidator;
	@Autowired
	private StatisticsValidator statisticsValidator;


	/**
	 * Add Method
	 * Used for storing a new {@Link Transaction}
	 * @param transaction
	 */
	@Override
	public void add(Transaction transaction) {
		transactionValidator.validateTransaction(transaction);

		final int position = this.getPositionWindow(transaction);
		synchronized (lockObj) {
			final Statistics statistics = statisticsCache.get(position);
			if (statisticsValidator.isValidStatistics(statistics.getTimestampInMillis())) {
				transactionCache.update(position, statistics, transaction);
			} else {
				transactionCache.insert(position, transaction);
			}

		}
	}

	@Override
	public void delete() {
		this.transactionCache.clear();
	}

	private int getPositionWindow(final Transaction transaction) {
		final long currentTime = System.currentTimeMillis();
		final int position = (int) ((currentTime - transaction.getTimestampInMillis()) / 1000);
		return position % 60;
	}
}
