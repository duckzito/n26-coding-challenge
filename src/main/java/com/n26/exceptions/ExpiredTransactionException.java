package com.n26.exceptions;

public class ExpiredTransactionException extends RuntimeException {

	public ExpiredTransactionException(String message) {
		super(message);
	}
}
