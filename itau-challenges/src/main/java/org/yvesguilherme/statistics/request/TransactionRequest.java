package org.yvesguilherme.statistics.request;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record TransactionRequest (@NotNull Double valor, @NotNull OffsetDateTime dataHora) {
}
