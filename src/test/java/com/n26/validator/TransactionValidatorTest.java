package com.n26.validator;

import com.n26.exceptions.ExpiredTransactionException;
import com.n26.exceptions.FutureTransactionException;
import com.n26.exceptions.InvalidTransactionException;
import com.n26.model.Transaction;
import com.n26.validators.TransactionValidator;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionValidatorTest {

	private TransactionValidator validator = new TransactionValidator(60000);

	@Test(expected = InvalidTransactionException.class)
	public void givenAEmptyTransactionWhenValidateThenAInvalidTransactionExceptionShouldBeThrown() {
		Transaction transaction = new Transaction();

		this.validator.validateTransaction(transaction);
	}

	@Test(expected = InvalidTransactionException.class)
	public void givenATransactionWithoutAmountWhenValidateThenAInvalidTransactionExceptionShouldBeThrown() {
		Transaction transaction = new Transaction();
		transaction.setTimestamp(LocalDateTime.now());

		this.validator.validateTransaction(transaction);
	}

	@Test(expected = InvalidTransactionException.class)
	public void givenATransactionWithoutTimestampWhenValidateThenAInvalidTransactionExceptionShouldBeThrown() {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(1234));

		this.validator.validateTransaction(transaction);
	}

	@Test(expected = FutureTransactionException.class)
	public void givenATransactionWithFutureTimestampWhenValidateThenAFutureTransactionExceptionShouldBeThrown() {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(1234));
		transaction.setTimestamp(LocalDateTime.now().plusDays(10));

		this.validator.validateTransaction(transaction);
	}

	@Test(expected = ExpiredTransactionException.class)
	public void givenATransactionOlderThan60SecondsWhenValidateThenAExpiredTransactionExceptionShouldBeThrown() {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(1234));
		transaction.setTimestamp(LocalDateTime.now().minusSeconds(61));

		this.validator.validateTransaction(transaction);
	}

	@Test
	public void givenAValidTransactionWhenValidateThenNOExceptionShouldBeThrown() {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(1234));
		transaction.setTimestamp(LocalDateTime.now());

		this.validator.validateTransaction(transaction);
	}

	@Test
	public void givenATransactionWhenValidateThenItShouldBeValid() {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(1234));
		transaction.setTimestamp(LocalDateTime.now());

		final Boolean validTransaction = this.validator.isStillValidTransaction(transaction);

		Assert.assertTrue(validTransaction);
	}

}
