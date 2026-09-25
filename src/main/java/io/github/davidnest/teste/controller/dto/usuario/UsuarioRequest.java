package io.github.davidnest.teste.controller.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioRequest(
        @NotBlank String password,
        @NotBlank String name,
        @NotBlank @Email String email,
        List<String> roles
) {
}
