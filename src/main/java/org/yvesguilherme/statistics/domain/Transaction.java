package org.yvesguilherme.statistics.domain;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record Transaction(@NotNull Double valor, @NotNull OffsetDateTime dataHora) {
}
