package org.yvesguilherme.statistics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.yvesguilherme.statistics.domain.Statistics;
import org.yvesguilherme.statistics.response.StatisticsResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatisticsMapper {
  StatisticsResponse toStatisticsResponse(Statistics statistics);
}
