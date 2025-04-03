package org.yvesguilherme.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yvesguilherme.statistics.config.StatisticsConfig;
import org.yvesguilherme.statistics.domain.Statistics;
import org.yvesguilherme.statistics.domain.Transaction;
import org.yvesguilherme.statistics.exception.UnprocessableEntityException;

import java.time.OffsetDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
  private final Queue<Transaction> transactionQueue = new ConcurrentLinkedQueue<>();
  private final StatisticsConfig statisticsConfig;

  public void addTransaction(Transaction transaction) {
    validateTransaction(transaction);

    transactionQueue.offer(transaction);
  }

  public void removeAllTransactions() {
    transactionQueue.clear();
  }

  public Statistics getStatistics() {
    var now = OffsetDateTime.now();
    var secondsAgo = now.minusSeconds(statisticsConfig.getTimeframeSeconds());

    var doubleSummaryStatistics = transactionQueue.stream()
            .filter(transaction -> transaction.dataHora().isAfter(secondsAgo))
            .mapToDouble(Transaction::valor)
            .summaryStatistics();

    if (doubleSummaryStatistics.getCount() == 0) {
      return new Statistics(0, 0.0, 0.0, 0.0, 0.0);
    }

    return new Statistics(doubleSummaryStatistics);
  }

  private static void validateTransaction(Transaction transaction) {
    var now = OffsetDateTime.now();

    if (transaction == null || transaction.valor() <= 0 || transaction.dataHora().isAfter(now)) {
      throw new UnprocessableEntityException();
    }
  }
}
