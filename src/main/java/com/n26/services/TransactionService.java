package com.n26.services;

import com.n26.model.Transaction;

/**
 * Transaction Service Interface
 */
public interface TransactionService {
	void add(Transaction transaction);
	void delete();
}
