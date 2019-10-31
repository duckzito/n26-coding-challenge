package com.n26.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


/**
 * Transaction Business Object
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"amount",
		"timestamp"
})
public class Transaction {

	private BigDecimal amount;
	private LocalDateTime timestamp;

	public Transaction() {
	}

	public Transaction(BigDecimal amount, LocalDateTime timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}

	@JsonIgnore
	public Long getTransactionAge() {
		return Duration.between(this.timestamp, LocalDateTime.now()).toMillis();
	}

	@JsonIgnore
	public Long getTimestampInMillis() {
		return this.timestamp.toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
