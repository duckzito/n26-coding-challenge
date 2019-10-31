package com.n26.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.n26.config.BigDecimalToStringJsonSerializer;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static com.n26.utils.StatisticsMapsUtils.*;

/**
 * Statistic Business Object
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"sum",
		"avg",
		"max",
		"min",
		"count"
})
public class Statistics {

	@JsonSerialize(using = BigDecimalToStringJsonSerializer.class)
	private BigDecimal sum;
	@JsonSerialize(using = BigDecimalToStringJsonSerializer.class)
	private BigDecimal avg;
	@JsonSerialize(using = BigDecimalToStringJsonSerializer.class)
	private BigDecimal max;
	@JsonSerialize(using = BigDecimalToStringJsonSerializer.class)
	private BigDecimal min;
	private Long count;

	@JsonIgnore
	private LocalDateTime timestamp;

	public Statistics() {
		sum = BigDecimal.ZERO;
		avg = BigDecimal.ZERO;
		max = BigDecimal.ZERO;
		min = BigDecimal.ZERO;
		count = 0L;
		timestamp = LocalDateTime.now();
	}

	public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, Long count) {
		this();
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}
	public Long getCount() {
		return count;
	}

	@JsonIgnore
	public Long getAge() {
		return Duration.between(this.timestamp, LocalDateTime.now()).toMillis();
	}

	public static Statistics build(Map<String, Object> statisticMap) {
		BigDecimal sum = (BigDecimal) statisticMap.get(SUM);
		BigDecimal min = (BigDecimal) statisticMap.get(MIN);
		BigDecimal max = (BigDecimal) statisticMap.get(MAX);
		BigDecimal avg = (BigDecimal) statisticMap.get(AVG);
		long count = (Long) statisticMap.get(COUNT);

		return new Statistics(sum, avg, max, min, count);
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
