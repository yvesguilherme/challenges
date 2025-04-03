package org.yvesguilherme.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.statistics.mapper.TransactionMapper;
import org.yvesguilherme.statistics.request.TransactionRequest;
import org.yvesguilherme.statistics.service.TransactionService;

@RestController
@RequestMapping("/transacao")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionMapper transactionMapper;

  @Operation(summary = "Create a transaction")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Transaction created successfully", content = {@Content(schema = @Schema())}),
          @ApiResponse(responseCode = "422", description = "Unprocessable entity, invalid request", content = {@Content(schema = @Schema())}),
          @ApiResponse(responseCode = "400", description = "Bad request, or something went wrong", content = {@Content(schema = @Schema())}),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema())})
  })
  @PostMapping
  public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
    log.info("## Creating transaction: {}", transactionRequest);
    var transaction = transactionMapper.toTransaction(transactionRequest);

    log.info("## Adding transaction: {}", transaction);
    transactionService.addTransaction(transaction);

    log.info("## Transaction added successfully");
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  private final TransactionService transactionService;

  @Operation(summary = "Remove all transactions")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "All transactions removed successfully", content = {@Content(schema = @Schema())}),
          @ApiResponse(responseCode = "400", description = "Bad request, or something went wrong", content = {@Content(schema = @Schema())}),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema())})
  })
  @DeleteMapping
  public ResponseEntity<Void> removeAllTransactions() {
    log.info("## Removing all transactions");
    transactionService.removeAllTransactions();

    log.info("## All transactions removed successfully");
    return ResponseEntity.ok().build();
  }
}
