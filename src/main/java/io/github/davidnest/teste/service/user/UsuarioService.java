package io.github.davidnest.teste.service.user;

import io.github.davidnest.teste.model.entity.Usuario;
import io.github.davidnest.teste.model.exception.usuario.UsuarioCadastradoException;
import io.github.davidnest.teste.model.exception.usuario.UsuarioNotFoundException;
import io.github.davidnest.teste.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public Usuario save(Usuario usuario){
        Optional<Usuario> usuarioEncontrado = findByEmail(usuario.getEmail());
        if(usuarioEncontrado.isPresent()){
            throw new UsuarioCadastradoException("Usuário já cadastrado!");
        }
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario findById(UUID id){
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario não encontrado! " + id));
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email){
        return repository.findByEmail(email);
    }

    @Transactional
    public Usuario update(UUID id, Usuario newUser){

        Usuario user = findById(id);
        Optional<Usuario> usuarioEmail = findByEmail(newUser.getEmail());
        //? SE O EMAIL ENVIADO ESTIVER SENDO USADO, LANÇA UsuarioCadastradoException
        if(usuarioEmail.isPresent() && !user.getId().equals(usuarioEmail.get().getId())){
            throw new UsuarioCadastradoException("Usuario já está em uso por outro usuário!");
        }

        user.setPassword(encoder.encode(newUser.getPassword()));
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setRoles(newUser.getRoles());

        return repository.save(user);
    }

    @Transactional(readOnly = true)
    public void deleteById(UUID id){
        findById(id);
        repository.deleteById(id);
    }
}

