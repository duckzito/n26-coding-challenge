package com.n26.utils;

import com.n26.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Statistics Utils
 */
public class StatisticsMapsUtils {

	public static final String SUM = "sum";
	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final String AVG = "avg";
	public static final String COUNT = "count";
	public static final String TIMESTAMP = "timestamp";

	private StatisticsMapsUtils() {
	}

	/**
	 * Returns a {@link Map<String, Object>} that represent a {@link Transaction}
	 *
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> toStatisticMap(final Transaction transaction) {
		final Map<String, Object> statisticsMap = new HashMap<>();

		statisticsMap.put(SUM, transaction.getAmount());
		statisticsMap.put(MIN, transaction.getAmount());
		statisticsMap.put(MAX, transaction.getAmount());
		statisticsMap.put(AVG, transaction.getAmount());
		statisticsMap.put(COUNT, 1L);
		statisticsMap.put(TIMESTAMP, transaction.getTimestampInMillis());

		return statisticsMap;
	}

	/**
	 * Returns a {@link Map<String, Object>} result of update and existing Statistic with a {@link Transaction}
	 *
	 * @return Map<String, Object>
	 */

	public static Map<String, Object> updateExisting(final Map<String, Object> statistics, final Transaction transaction) {
		Map<String, Object> updatedStatistic = new HashMap<>();

		BigDecimal sum = get(SUM, statistics);
		BigDecimal min = get(MIN, statistics);
		BigDecimal max = get(MAX, statistics);

		Long count = (Long) statistics.get(COUNT);

		final BigDecimal updatedSum = sum.add(transaction.getAmount());
		final long updatedCount = count + 1L;
		final BigDecimal updatedAvg = updatedSum.divide(BigDecimal.valueOf(updatedCount), 2, RoundingMode.HALF_UP);
		final BigDecimal updatedMin = transaction.getAmount().min(min);
		final BigDecimal updatedMax = transaction.getAmount().max(max);

		updatedStatistic.put(SUM, updatedSum);
		updatedStatistic.put(MIN, updatedMin);
		updatedStatistic.put(MAX, updatedMax);
		updatedStatistic.put(AVG, updatedAvg);
		updatedStatistic.put(COUNT, updatedCount);
		updatedStatistic.put(TIMESTAMP, transaction.getTimestampInMillis());

		return updatedStatistic;
	}

	/**
	 * Returns a {@link Map<String, Object>} result of merge and existing Statistic with another Statistic
	 *
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> mergeStatistics(final Map<String, Object> statistics, final Map<String, Object> statistics2) {

		Map<String, Object> updatedStatistic = new HashMap<>();

		BigDecimal sum = get(SUM, statistics);
		BigDecimal min = get(MIN, statistics);
		BigDecimal max = get(MAX, statistics);
		long count = (long) statistics.get(COUNT);

		BigDecimal sum2 = get(SUM, statistics2);
		BigDecimal min2 = get(MIN, statistics2);
		BigDecimal max2 = get(MAX, statistics2);
		long count2 = (long) statistics2.get(COUNT);
		long timestamp = (long) statistics2.get(TIMESTAMP);

		BigDecimal updatedSum = sum.add(sum2);
		long updatedCount = count + count2;
		BigDecimal updatedAvg = updatedSum.divide(BigDecimal.valueOf(updatedCount), 2, RoundingMode.HALF_UP);
		BigDecimal updatedMin = min.min(min2);
		BigDecimal updatedMax = max.max(max2);

		updatedStatistic.put(SUM, updatedSum);
		updatedStatistic.put(MIN, updatedMin);
		updatedStatistic.put(MAX, updatedMax);
		updatedStatistic.put(AVG, updatedAvg);
		updatedStatistic.put(COUNT, updatedCount);
		updatedStatistic.put(TIMESTAMP, timestamp);

		return updatedStatistic;
	}

	private static BigDecimal get(String key, final Map<String, Object> statistics) {
		return (BigDecimal) statistics.get(key);
	}
}
