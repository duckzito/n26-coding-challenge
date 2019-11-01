package com.n26.repositories.impl;

import com.n26.exceptions.FutureTransactionException;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repositories.impl.TransactionMapCacheImpl;
import com.n26.validators.StatisticsValidator;
import com.n26.validators.TransactionValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TransactionCacheTest {

	@Mock
	private StatisticsValidator validator;
	@Mock
	private TransactionValidator transactionValidator;

	@InjectMocks
	private TransactionMapCacheImpl transactionMapCache;

	@Before
	public void setup() {
		this.transactionMapCache.initialize();
	}

	@Test
	public void givenAValidTransactionWhenIsAddedToCacheThenCacheShouldContainIt() {
		BigDecimal amount = new BigDecimal("300.00");

		final Transaction transaction = new Transaction(amount, LocalDateTime.now());

		transactionMapCache.insert(0, transaction);

		final Statistics result = transactionMapCache.concurrentHashMap.values()
				.stream()
				.findAny()
				.get();

		Assert.assertEquals(amount, result.getSum());
		Assert.assertEquals(amount, result.getAvg());
		Assert.assertEquals(amount, result.getMax());
		Assert.assertEquals(amount, result.getMin());
		Assert.assertEquals(1L, result.getCount().longValue());
	}

	@Test
	public void given2ValidTransactionsWhenAreAddedToCacheThenCacheSizeShouldBeTwo() {
		BigDecimal amount = new BigDecimal("300.00");
		BigDecimal zero = BigDecimal.ZERO;
		final LocalDateTime now = LocalDateTime.now();

		final Transaction transaction = new Transaction(amount, now);
		final Transaction transaction1 = new Transaction(amount, now.minusSeconds(10));

		transactionMapCache.insert(0, transaction);
		transactionMapCache.insert(1, transaction1);

		final List<Statistics> result = transactionMapCache.concurrentHashMap.values()
				.stream()
				.filter(s -> s.getSum().compareTo(zero) != 0)
				.collect(Collectors.toList());

		Assert.assertEquals(2, result.size());
	}

}
