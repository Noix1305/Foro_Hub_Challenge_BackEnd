package com.forohub.foro.controller;

import com.forohub.foro.domain.perfil.Perfil;
import com.forohub.foro.domain.perfil.PerfilRepository;
import com.forohub.foro.domain.usuario.DatosRegistroUsuario;
import com.forohub.foro.domain.usuario.Usuario;
import com.forohub.foro.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/signin")
public class RegistroController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public void registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario) {
        // Verificar si ya existe un tópico con el mismo título y mensaje
        Perfil peril = perfilRepository.findById(datosRegistroUsuario.perfil_id())
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado"));

        Usuario usuario = new Usuario(datosRegistroUsuario, passwordEncoder, peril);
        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el mismo correo electronico.");
        }

        usuarioRepository.save(usuario);
    }
}
