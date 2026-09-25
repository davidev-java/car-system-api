package io.github.davidnest.teste.controller.dto.purchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PurchaseResponse(
        UUID id,
        UUID carId,
        String carName,
        BigDecimal amount,
        BigDecimal balanceAfter,
        LocalDateTime createdAt
){}
