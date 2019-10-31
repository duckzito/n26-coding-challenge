package com.n26.utils;

import com.n26.model.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

import static com.n26.utils.StatisticsMapsUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StatisticsMapsUtilsTest {

	@Test
	public void givenATransactionWhenToStatisticMapIsCalledThenAValidMapShouldBeReturned() {
		BigDecimal amount = new BigDecimal(1234);
		LocalDateTime now = LocalDateTime.now();

		Transaction t1 = new Transaction(amount, now);

		final Map<String, Object> statistics = StatisticsMapsUtils.toStatisticMap(t1);

		BigDecimal sum = (BigDecimal) statistics.get(SUM);
		BigDecimal min = (BigDecimal) statistics.get(MIN);
		BigDecimal max = (BigDecimal) statistics.get(MAX);
		BigDecimal avg = (BigDecimal) statistics.get(AVG);
		long count = (long) statistics.get(COUNT);

		assertFalse(statistics.isEmpty());
		assertEquals(amount, sum);
		assertEquals(amount, min);
		assertEquals(amount, max);
		assertEquals(amount, avg);
		assertEquals(1L, count);
	}

	@Test
	public void givenATransactionAndAnExistingStatisticWhenUpdateExistingIsCalledThenANewUpdatedStatisticShoudBeReturned() {
		BigDecimal amount = new BigDecimal(1234);
		LocalDateTime now = LocalDateTime.now();

		Transaction t1 = new Transaction(amount, now);

		final Map<String, Object> originalStatistic = StatisticsMapsUtils.toStatisticMap(t1);

		BigDecimal amountT2 = new BigDecimal(5678);
		Transaction t2 = new Transaction(amountT2, now.plusSeconds(30));

		final Map<String, Object> updatedStatistic = updateExisting(originalStatistic, t2);

		assertFalse(updatedStatistic.isEmpty());

		BigDecimal finalAmount = amount.add(amountT2);
		BigDecimal avgAmount = finalAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

		BigDecimal sum = (BigDecimal) updatedStatistic.get(SUM);
		BigDecimal min = (BigDecimal) updatedStatistic.get(MIN);
		BigDecimal max = (BigDecimal) updatedStatistic.get(MAX);
		BigDecimal avg = (BigDecimal) updatedStatistic.get(AVG);
		long count = (long) updatedStatistic.get(COUNT);

		assertEquals(finalAmount, sum);
		assertEquals(amount, min);
		assertEquals(amountT2, max);
		assertEquals(avgAmount, avg);
		assertEquals(2L, count);
	}

	@Test
	public void givenAnExistingStatisticAndANewStatistictWhenMergeStatisticsIsCalledThenANewAccumulatedStatisticShouldBeReturned() {
		BigDecimal amount = new BigDecimal(1234);
		LocalDateTime now = LocalDateTime.now();
		Transaction t1 = new Transaction(amount, now);
		final Map<String, Object> originalStatistic = StatisticsMapsUtils.toStatisticMap(t1);

		BigDecimal amountT2 = new BigDecimal(5678);
		Transaction t2 = new Transaction(amountT2, now.plusSeconds(30));
		final Map<String, Object> secondStatistic = StatisticsMapsUtils.toStatisticMap(t2);

		final Map<String, Object> mergedStatistic = mergeStatistics(originalStatistic, secondStatistic);

		BigDecimal sum = (BigDecimal) mergedStatistic.get(SUM);
		BigDecimal min = (BigDecimal) mergedStatistic.get(MIN);
		BigDecimal max = (BigDecimal) mergedStatistic.get(MAX);
		BigDecimal avg = (BigDecimal) mergedStatistic.get(AVG);
		long count = (long) mergedStatistic.get(COUNT);

		BigDecimal finalAmount = amount.add(amountT2);
		BigDecimal avgAmount = finalAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

		assertEquals(finalAmount, sum);
		assertEquals(amount, min);
		assertEquals(amountT2, max);
		assertEquals(avgAmount, avg);
		assertEquals(2L, count);


	}

}
