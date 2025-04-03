package org.yvesguilherme.statistics.domain;

import java.util.DoubleSummaryStatistics;

public record Statistics(long count, double sum, double avg, double min, double max) {
  public Statistics(DoubleSummaryStatistics doubleSummaryStatistics) {
    this(
            doubleSummaryStatistics.getCount(),
            doubleSummaryStatistics.getSum(),
            doubleSummaryStatistics.getAverage(),
            doubleSummaryStatistics.getMin(),
            doubleSummaryStatistics.getMax()
    );
  }
}
