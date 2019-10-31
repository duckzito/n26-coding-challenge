package com.n26.validator;


import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.validators.StatisticsValidator;
import com.n26.validators.TransactionValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StatisticsValidatorTest {

	private StatisticsValidator validator = new StatisticsValidator(60000);

	@Test
	public void givenATransactionWhenValidateThenItShouldBeValid() {
		Statistics statistic = new Statistics();
		statistic.setTimestamp(LocalDateTime.now());

		final Boolean validTransaction = this.validator.isValidStatistics(statistic);

		Assert.assertTrue(validTransaction);
	}
}
