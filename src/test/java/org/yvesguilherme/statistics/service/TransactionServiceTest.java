package org.yvesguilherme.statistics.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yvesguilherme.statistics.config.StatisticsConfig;
import org.yvesguilherme.statistics.domain.Transaction;
import org.yvesguilherme.statistics.exception.UnprocessableEntityException;

import java.time.OffsetDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionServiceTest {

  @InjectMocks
  private TransactionService service;

  @Mock
  private StatisticsConfig statisticsConfig;

  @Test
  @DisplayName("addTransaction adds a transaction to the queue when successful")
  @Order(1)
  void addTransaction_AddsATransactionToTheQueue_WhenSuccessful() {
    BDDMockito.when(statisticsConfig.getTimeframeSeconds()).thenReturn(60);

    Transaction transaction = new Transaction(100.0, OffsetDateTime.now().minusSeconds(30));
    service.addTransaction(transaction);

    Assertions.assertThat(service.getStatistics().count()).isEqualTo(1);
  }

  @ParameterizedTest
  @MethodSource("provideInvalidTransactions")
  @DisplayName("addTransaction throws UnprocessableEntityException when transaction is invalid")
  @Order(2)
  void addTransaction_ThrowsUnprocessableEntityException_WhenTransactionIsInvalid(Transaction transaction) {
    Assertions
            .assertThatThrownBy(() -> service.addTransaction(transaction))
            .isInstanceOf(UnprocessableEntityException.class);
  }

  private static Stream<Arguments> provideInvalidTransactions() {
    return Stream.of(
            Arguments.of(new Transaction(0.0, OffsetDateTime.now().minusSeconds(30))),
            Arguments.of(new Transaction(-1.0, OffsetDateTime.now().minusSeconds(30))),
            Arguments.of(new Transaction(100.0, OffsetDateTime.now().plusSeconds(30))),
            null
    );
  }

  @Test
  @DisplayName("removeAllTransactions clears the transaction queue when successful")
  @Order(3)
  void removeAllTransactions_ClearsTheTransactionQueue_WhenSuccessful() {
    Transaction transaction = new Transaction(100.0, OffsetDateTime.now().minusSeconds(30));
    service.addTransaction(transaction);
    service.removeAllTransactions();

    Assertions.assertThat(service.getStatistics().count()).isZero();
  }

  @Test
  @DisplayName("getStatistics returns statistics of transactions in the last minute when successful")
  @Order(4)
  void getStatistics_ReturnsStatisticsOfTransactionsInTheLastMinute_WhenSuccessful() {
    BDDMockito.when(statisticsConfig.getTimeframeSeconds()).thenReturn(60);

    var transaction1 = new Transaction(100.0, OffsetDateTime.now().minusSeconds(30));
    var transaction2 = new Transaction(200.0, OffsetDateTime.now().minusSeconds(10));
    service.addTransaction(transaction1);
    service.addTransaction(transaction2);

    var statistics = service.getStatistics();

    Assertions.assertThat(statistics.count()).isEqualTo(2);
    Assertions.assertThat(statistics.sum()).isEqualTo(300.0);
    Assertions.assertThat(statistics.avg()).isEqualTo(150.0);
    Assertions.assertThat(statistics.min()).isEqualTo(100.0);
    Assertions.assertThat(statistics.max()).isEqualTo(200.0);
  }

  @Test
  @DisplayName("getStatistics returns empty statistics when no transactions in the last minute")
  @Order(5)
  void getStatistics_ReturnsEmptyStatistics_WhenNoTransactionsInTheLastMinute() {
    var transaction1 = new Transaction(100.0, OffsetDateTime.now().minusMinutes(2));
    service.addTransaction(transaction1);

    var statistics = service.getStatistics();

    Assertions.assertThat(statistics.count()).isZero();
    Assertions.assertThat(statistics.sum()).isZero();
    Assertions.assertThat(statistics.avg()).isZero();
    Assertions.assertThat(statistics.min()).isZero();
    Assertions.assertThat(statistics.max()).isZero();
  }

  @Test
  @DisplayName("getStatistics test performance")
  void getStatistics_TestPerformance() throws InterruptedException {
    BDDMockito.when(statisticsConfig.getTimeframeSeconds()).thenReturn(60);

    int numberOfTransactions = 1_000_000;
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    CountDownLatch countDownLatch = new CountDownLatch(numberOfTransactions);

    long startTime = System.currentTimeMillis();

    for (int i = 0; i < numberOfTransactions; i++) {
      executor.submit(() -> {
        service.addTransaction(new Transaction(100.0, OffsetDateTime.now().minusSeconds(30)));
        countDownLatch.countDown();
      });
    }

    countDownLatch.await(30, TimeUnit.SECONDS);
    long endTime = System.currentTimeMillis();

    Assertions.assertThat(endTime - startTime).isLessThan(2000);
    Assertions.assertThat(service.getStatistics().count()).isEqualTo(numberOfTransactions);
  }
}