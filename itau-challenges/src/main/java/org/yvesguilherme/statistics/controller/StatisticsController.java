package org.yvesguilherme.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yvesguilherme.statistics.mapper.StatisticsMapper;
import org.yvesguilherme.statistics.response.StatisticsResponse;
import org.yvesguilherme.statistics.service.TransactionService;

@RestController
@RequestMapping("/estatistica")
@Slf4j
@RequiredArgsConstructor
public class StatisticsController {
  private final StatisticsMapper statisticsMapper;
  private final TransactionService transactionService;

  @Operation(summary = "Get statistics")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Statistics retrieved successfully",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = StatisticsResponse.class)
                          )
                  }
          ),
          @ApiResponse(responseCode = "400", description = "Bad request, or something went wrong", content = @Content()),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
  })
  @GetMapping
  public ResponseEntity<StatisticsResponse> getStatistics() {
    log.info("## Getting statistics...");
    var statistics = transactionService.getStatistics();

    log.info("## Transforming statistics to response...");
    var statisticsResponse = statisticsMapper.toStatisticsResponse(statistics);

    log.info("## Statistics response: {}", statisticsResponse);
    return ResponseEntity.ok(statisticsResponse);
  }
}
