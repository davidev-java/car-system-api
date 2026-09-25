package io.github.davidnest.teste.controller.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ClientRequest(
        @NotBlank String name,
        @NotNull @PositiveOrZero BigDecimal balance,
        List<UUID> carIds
) {
}
