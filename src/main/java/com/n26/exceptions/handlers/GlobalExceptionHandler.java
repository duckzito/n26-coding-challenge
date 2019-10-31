package com.n26.exceptions.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.exceptions.ExpiredTransactionException;
import com.n26.exceptions.FutureTransactionException;
import com.n26.exceptions.InvalidTransactionException;
import com.n26.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 *  Exception handler to manage appropriate HTTP response codes
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String INVALID_JSON = "Invalid JSON";
	private static final String INCORRECT_NUMBER_FORMAT = "Incorrect Number Format";
	private static final String TRANSACTION_EXPIRED = "Transaction Expired";

	@ExceptionHandler(ExpiredTransactionException.class)
	public final ResponseEntity<Object> handleExpiredTransactionException(ExpiredTransactionException ex, WebRequest request) {
		return this.buildErrorResponse(TRANSACTION_EXPIRED, ex.getLocalizedMessage(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler({NumberFormatException.class, InvalidFormatException.class,
			FutureTransactionException.class, JsonMappingException.class, InvalidTransactionException.class, DateTimeParseException.class})
	public final ResponseEntity<Object> handleUnprocessableEnityExceptions(Exception ex, WebRequest request) {
		return this.buildErrorResponse(INCORRECT_NUMBER_FORMAT, ex.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		if (ex.getCause() instanceof InvalidFormatException) {
			return this.buildErrorResponse(INVALID_JSON, ex.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		return this.buildErrorResponse(INVALID_JSON, ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity buildErrorResponse(String message, String localizedMessage, HttpStatus status) {
		final ErrorResponse error = new ErrorResponse(LocalDateTime.now(), message, localizedMessage);
		return new ResponseEntity(error, status);
	}
}
