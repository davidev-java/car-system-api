package io.github.davidnest.teste.controller.dto.message;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseError(
        Integer status,
        String message,
        LocalDateTime timestamp,
        List<ValidationFieldError> errors) {
}
