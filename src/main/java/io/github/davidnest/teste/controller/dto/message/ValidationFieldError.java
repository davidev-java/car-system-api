package io.github.davidnest.teste.controller.dto.message;

public record ValidationFieldError(
        String field,
        String message
) {
}
