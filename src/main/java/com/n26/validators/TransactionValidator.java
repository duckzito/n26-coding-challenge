package com.n26.validators;

import com.n26.exceptions.ExpiredTransactionException;
import com.n26.exceptions.FutureTransactionException;
import com.n26.exceptions.InvalidTransactionException;
import com.n26.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Transaction Validator
 */
@Slf4j
@Component
public class TransactionValidator {

	private long ttl;

	@Autowired
	public TransactionValidator(@Value("${ttl}") long ttl) {
		this.ttl = ttl;
	}

	public void validateTransaction(final Transaction transaction) {
		log.info("Validating transaction");
		if (Objects.isNull(transaction.getAmount()) || Objects.isNull(transaction.getTimestamp())) {
			throw new InvalidTransactionException("Invalid Transaction");
		}

		if (transaction.getTransactionAge() < 0) {
			throw new FutureTransactionException("Future transaction not allowed");
		} else if (transaction.getTransactionAge() > ttl) {
			throw new ExpiredTransactionException("Expired transaction");
		}
	}

	public Boolean isStillValidTransaction(Transaction transaction) {
		return transaction.getTransactionAge() <= ttl;
	}
}
