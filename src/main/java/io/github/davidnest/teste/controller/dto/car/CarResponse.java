package io.github.davidnest.teste.controller.dto.car;

import java.math.BigDecimal;
import java.util.UUID;

public record CarResponse(
        UUID id,
        String name,
        String model,
        Integer hp,
        Integer year,
        BigDecimal price,
        UUID clientId
) {}
