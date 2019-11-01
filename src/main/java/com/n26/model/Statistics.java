package com.n26.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.n26.config.BigDecimalToStringJsonSerializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

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
public @Getter @Setter class Statistics {

	public static final String SUM = "sum";
	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final String AVG = "avg";
	public static final String COUNT = "count";
	public static final String TIMESTAMP = "timestamp";


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

	public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, Long count, LocalDateTime timestamp) {
		this();
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
		this.timestamp = timestamp;
	}
	public Long getCount() {
		return count;
	}

	@JsonIgnore
	public Long getAge() {
		return Duration.between(this.timestamp, LocalDateTime.now()).toMillis();
	}

	@JsonIgnore
	public Long getTimestampInMillis() {
		return this.timestamp.toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	/**
	 * Returns a {@link Map<String, Object>} that represent a {@link Transaction}
	 *
	 * @return Map<String, Object>
	 */
	public static Statistics buildFrom(final Transaction transaction) {
		return new Statistics(transaction.getAmount(),
				transaction.getAmount(),
				transaction.getAmount(),
				transaction.getAmount(),
				1L,
				transaction.getTimestamp());
	}

	/**
	 * Returns a {@link Map<String, Object>} result of update and existing Statistic with a {@link Transaction}
	 *
	 * @return Map<String, Object>
	 */

	public static Statistics updateExisting(final Statistics statistics, final Transaction transaction) {
		Map<String, Object> updatedStatistic = new HashMap<>();

		BigDecimal sum = statistics.getSum();
		BigDecimal min = statistics.getMin();
		BigDecimal max = statistics.getMax();
		Long count = statistics.getCount();

		final BigDecimal updatedSum = sum.add(transaction.getAmount());
		final long updatedCount = count + 1L;
		final BigDecimal updatedAvg = updatedSum.divide(BigDecimal.valueOf(updatedCount), 2, RoundingMode.HALF_UP);

		BigDecimal updatedMin = transaction.getAmount().min(min);
		if (min.compareTo(BigDecimal.ZERO) == 0) {
			updatedMin = updatedMin.add(transaction.getAmount());
		}

		final BigDecimal updatedMax = transaction.getAmount().max(max);

		//public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, Long count, LocalDateTime timestamp) {
		return new Statistics(
				updatedSum,
				updatedAvg,
				updatedMax,
				updatedMin,
				updatedCount,
				transaction.getTimestamp());
	}

	/**
	 * Returns a {@link Map<String, Object>} result of merge and existing Statistic with another Statistic
	 *
	 * @return Map<String, Object>
	 */
	public static Statistics mergeStatistics(final Statistics statistics, final Statistics statistics2) {

		BigDecimal sum = statistics.getSum();
		BigDecimal min = statistics.getMin();
		BigDecimal max = statistics.getMax();
		Long count = statistics.getCount();

		BigDecimal sum2 = statistics2.getSum();
		BigDecimal min2 = statistics2.getMin();
		BigDecimal max2 = statistics2.getMax();
		Long count2 = statistics2.getCount();
		LocalDateTime timestamp = statistics2.getTimestamp();

		BigDecimal updatedSum = sum.add(sum2);
		long updatedCount = count + count2;
		BigDecimal updatedAvg = updatedSum.divide(BigDecimal.valueOf(updatedCount), 2, RoundingMode.HALF_UP);
		BigDecimal updatedMin = min.min(min2);
		BigDecimal updatedMax = max.max(max2);

		return new Statistics(
				updatedSum,
				updatedAvg,
				updatedMax,
				updatedMin,
				updatedCount,
				timestamp);
	}

}
