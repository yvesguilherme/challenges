package org.yvesguilherme.statistics.response;

import java.util.DoubleSummaryStatistics;

public record StatisticsResponse(long count, double sum, double avg, double min, double max) {
  public StatisticsResponse(DoubleSummaryStatistics doubleSummaryStatistics) {
    this(
            doubleSummaryStatistics.getCount(),
            doubleSummaryStatistics.getSum(),
            doubleSummaryStatistics.getAverage(),
            doubleSummaryStatistics.getMin(),
            doubleSummaryStatistics.getMax()
    );
  }
}
