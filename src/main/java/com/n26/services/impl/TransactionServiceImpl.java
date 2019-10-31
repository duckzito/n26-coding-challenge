package com.n26.services.impl;

import com.n26.model.Transaction;
import com.n26.repositories.TransactionCache;
import com.n26.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Transaction Service Implementation to handle add and delete transactions.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionCache transactionCache;

	@Override
	public void add(Transaction transaction) {
		transactionCache.put(transaction);
	}

	@Override
	public void delete() {
		this.transactionCache.clear();
	}
}
