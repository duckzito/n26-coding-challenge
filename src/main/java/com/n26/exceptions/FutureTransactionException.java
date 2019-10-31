package com.n26.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FutureTransactionException extends RuntimeException {

	public FutureTransactionException(String message) {
		super(message);
	}
}
