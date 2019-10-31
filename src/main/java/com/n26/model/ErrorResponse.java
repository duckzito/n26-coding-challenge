package com.n26.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


public class ErrorResponse {
	private LocalDateTime timestamp;
	private String message;
	private String details;

	public ErrorResponse() {
	}

	public ErrorResponse(LocalDateTime timestamp, String message, String details) {
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "ErrorResponse{" +
				"timestamp=" + timestamp +
				", message='" + message + '\'' +
				", details='" + details + '\'' +
				'}';
	}
}
