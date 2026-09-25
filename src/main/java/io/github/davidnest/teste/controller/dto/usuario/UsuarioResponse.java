package io.github.davidnest.teste.controller.dto.usuario;

import java.util.List;
import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String name,
        String email,
        List<String> roles
) {
}
