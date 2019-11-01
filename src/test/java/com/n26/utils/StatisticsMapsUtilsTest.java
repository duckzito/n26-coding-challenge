package com.n26.utils;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StatisticsMapsUtilsTest {

	@Test
	public void givenATransactionWhenToStatisticMapIsCalledThenAValidMapShouldBeReturned() {
		BigDecimal amount = new BigDecimal(1234);
		LocalDateTime now = LocalDateTime.now();

		Transaction t1 = new Transaction(amount, now);

		final Statistics statistics = Statistics.buildFrom(t1);

		assertEquals(amount, statistics.getSum());
		assertEquals(amount, statistics.getMin());
		assertEquals(amount, statistics.getMax());
		assertEquals(amount, statistics.getAvg());
		assertEquals(1L, statistics.getCount().longValue());
	}

	@Test
	public void givenATransactionAndAnExistingStatisticWhenUpdateExistingIsCalledThenANewUpdatedStatisticShoudBeReturned() {
		BigDecimal amount = new BigDecimal(1234);
		LocalDateTime now = LocalDateTime.now();

		Transaction t1 = new Transaction(amount, now);

		final Statistics originalStatistic = Statistics.buildFrom(t1);

		BigDecimal amountT2 = new BigDecimal(5678);
		Transaction t2 = new Transaction(amountT2, now.plusSeconds(30));

		final Statistics updatedStatistic = Statistics.updateExisting(originalStatistic, t2);

		BigDecimal finalAmount = amount.add(amountT2);
		BigDecimal avgAmount = finalAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

		BigDecimal sum = (BigDecimal) updatedStatistic.getSum();
		BigDecimal min = (BigDecimal) updatedStatistic.getMin();
		BigDecimal max = (BigDecimal) updatedStatistic.getMax();
		BigDecimal avg = (BigDecimal) updatedStatistic.getAvg();
		long count = updatedStatistic.getCount();

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
		final Statistics originalStatistic = Statistics.buildFrom(t1);

		BigDecimal amountT2 = new BigDecimal(5678);
		Transaction t2 = new Transaction(amountT2, now.plusSeconds(30));
		final Statistics secondStatistic = Statistics.buildFrom(t2);

		final Statistics  mergedStatistic = Statistics.mergeStatistics(originalStatistic, secondStatistic);

		BigDecimal sum = (BigDecimal) mergedStatistic.getSum();
		BigDecimal min = (BigDecimal) mergedStatistic.getMin();
		BigDecimal max = (BigDecimal) mergedStatistic.getMax();
		BigDecimal avg = (BigDecimal) mergedStatistic.getAvg();
		long count = mergedStatistic.getCount();

		BigDecimal finalAmount = amount.add(amountT2);
		BigDecimal avgAmount = finalAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

		assertEquals(finalAmount, sum);
		assertEquals(amount, min);
		assertEquals(amountT2, max);
		assertEquals(avgAmount, avg);
		assertEquals(2L, count);


	}

}
