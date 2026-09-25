package io.github.davidnest.teste.controller.dto.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String name,
        BigDecimal balance,
        List<UUID> carIds) {
}
