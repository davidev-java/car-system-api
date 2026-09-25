package io.github.davidnest.teste.controller;

import io.github.davidnest.teste.controller.dto.usuario.UsuarioRequest;
import io.github.davidnest.teste.controller.dto.usuario.UsuarioResponse;
import io.github.davidnest.teste.model.entity.Usuario;
import io.github.davidnest.teste.service.user.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    @PostMapping
    public ResponseEntity<UsuarioResponse> save(@RequestBody @Valid UsuarioRequest request){
        Usuario usuario = service.save(toEntity(request));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(toResponse(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse>  findUserById(@PathVariable UUID id){
        Usuario usuario = service.findById(id);
        return ResponseEntity.ok(toResponse(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> findAll(){
        List<UsuarioResponse> list = service.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable UUID id, @RequestBody @Valid UsuarioRequest request){
        Usuario user = service.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioResponse toResponse(Usuario usuario){
        return new UsuarioResponse
                (usuario.getId(), usuario.getName(), usuario.getEmail(), usuario.getRoles());

    }

    private Usuario toEntity(UsuarioRequest request){
        Usuario usuario = new Usuario();
        usuario.setPassword(request.password());
        usuario.setName(request.name());
        usuario.setEmail(request.email());
        usuario.setRoles(request.roles());
        return usuario;
    }

}
