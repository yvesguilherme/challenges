package org.yvesguilherme.statistics.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class StatisticsConfig {

  @Value("${statistics.timeframe.seconds}")
  private int timeframeSeconds;

}
