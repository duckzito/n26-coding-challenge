package com.n26.services;

import com.n26.exceptions.FutureTransactionException;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repositories.impl.TransactionMapCacheImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticsServiceTest {
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionMapCacheImpl transactionCache;
	@Autowired
	private StatisticsService statisticsService;

	@Test
	public void givenActualTransactionsWhenGetStatisticsThenValidStatisticsShouldBeReturned() {

		final LocalDateTime now = LocalDateTime.now();
		Transaction t1 = new Transaction(new BigDecimal("1000.00"), now);
		Transaction t2 = new Transaction(new BigDecimal("500.00"), now);
		Transaction t3 = new Transaction(new BigDecimal("200.00"), now);
		Transaction t4 = new Transaction(new BigDecimal("700.00"), now);
		Transaction t5 = new Transaction(new BigDecimal("150.00"), now);

		transactionCache.clear();

		transactionCache.insert(0, t1);
		transactionCache.insert(1, t2);
		transactionCache.insert(2, t3);
		transactionCache.insert(3, t4);
		transactionCache.insert(4, t5);

		final Statistics statistics = statisticsService.get();

		Assert.assertEquals(Long.valueOf(5), statistics.getCount());

		Assert.assertEquals(new BigDecimal("2550.00"), statistics.getSum());
		Assert.assertEquals(new BigDecimal("150.00"), statistics.getMin());
		Assert.assertEquals(new BigDecimal("1000.00"), statistics.getMax());
		Assert.assertEquals(new BigDecimal("510.00"), statistics.getAvg());

	}


	@Test
	public void givenPastTransactionsWhenGetStatisticsThenValidStatisticsShouldBeReturned() {
		final LocalDateTime now = LocalDateTime.now();
		Transaction t1 = new Transaction(new BigDecimal("1000.00"), now.minusSeconds(10));
		Transaction t2 = new Transaction(new BigDecimal("500.00"), now.minusSeconds(8));
		Transaction t3 = new Transaction(new BigDecimal("200.00"), now.minusSeconds(6));
		Transaction t4 = new Transaction(new BigDecimal("700.00"), now.minusSeconds(4));
		Transaction t5 = new Transaction(new BigDecimal("150.00"), now.minusSeconds(2));

		transactionService.delete();

		transactionService.add(t1);
		transactionService.add(t2);
		transactionService.add(t3);
		transactionService.add(t4);
		transactionService.add(t5);

		final Statistics statistics = statisticsService.get();

		Assert.assertEquals(Long.valueOf(5), statistics.getCount());

		Assert.assertEquals(new BigDecimal("2550.00"), statistics.getSum());
		Assert.assertEquals(new BigDecimal("150.00"), statistics.getMin());
		Assert.assertEquals(new BigDecimal("1000.00"), statistics.getMax());
		Assert.assertEquals(new BigDecimal("510.00"), statistics.getAvg());

	}

	@Test(expected = FutureTransactionException.class)
	public void givenFurureTransactionsWhenGetStatisticsThenValidStatisticsShouldBeReturned() {
		final LocalDateTime now = LocalDateTime.now();
		Transaction t1 = new Transaction(new BigDecimal("1000.00"), now);
		Transaction t2 = new Transaction(new BigDecimal("500.00"), now.plusSeconds(8));
		Transaction t3 = new Transaction(new BigDecimal("200.00"), now.plusSeconds(6));
		Transaction t4 = new Transaction(new BigDecimal("700.00"), now.plusSeconds(4));
		Transaction t5 = new Transaction(new BigDecimal("150.00"), now.plusSeconds(2));

		transactionService.delete();

		transactionService.add(t1);
		transactionService.add(t2);
		transactionService.add(t3);
		transactionService.add(t4);
		transactionService.add(t5);
	}
}
