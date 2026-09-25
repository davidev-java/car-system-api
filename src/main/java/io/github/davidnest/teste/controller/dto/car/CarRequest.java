package io.github.davidnest.teste.controller.dto.car;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CarRequest(
       @NotBlank String name,
       @NotBlank String model,
       @Positive @NotNull Integer hp,
       @NotNull Integer year,
       @NotNull @Positive BigDecimal price,
       UUID clientId
) {}
