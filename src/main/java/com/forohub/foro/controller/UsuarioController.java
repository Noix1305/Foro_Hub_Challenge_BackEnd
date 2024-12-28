package com.forohub.foro.controller;

import com.forohub.foro.domain.curso.Curso;
import com.forohub.foro.domain.topico.DatosListadoTopico;
import com.forohub.foro.domain.usuario.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PutMapping("/desativar/{id}")
    @Transactional
    public ResponseEntity desactivarUsuario(@PathVariable Long id) {

        Usuario usuario = null;

        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            usuario = usuarioOPT.get();
        }

        usuario.inactivarUsuario();

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activar/{id}")
    @Transactional
    public ResponseEntity activarUsuario(@PathVariable Long id) {

        Usuario usuario = null;

        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            usuario = usuarioOPT.get();
        }

        usuario.activarUsuario();

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizaCorreo/{id}")
    @Transactional
    public ResponseEntity cambiarCorreo(@PathVariable Long id, @RequestBody DatosActualizarCorreoUsuario datosActualizarCorreoUsuario) {

        Usuario usuario = null;

        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            usuario = usuarioOPT.get();
        }

        usuario.actualizarCorreo(datosActualizarCorreoUsuario);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(new DatosActualizarCorreoUsuario(usuario.getCorreoElectronico()));

    }

    @PutMapping("/cambiarContrasena/{id}")
    @Transactional
    public ResponseEntity cambiarContrasena(@PathVariable Long id, @RequestBody DatosActualizarContrasenaUsuario datosActualizarContrasenaUsuario) {

        Usuario usuario = null;

        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            usuario = usuarioOPT.get();
        }

        // Contraseña actual ingresada por el usuario
        String contrasenaActualIngresada = datosActualizarContrasenaUsuario.contrasenaActual();

        // Validar si la contraseña ingresada coincide con la contraseña almacenada en la base de datos
        if (!passwordEncoder.matches(contrasenaActualIngresada, usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contraseña actual incorrecta");
        }

        // Validar si la nueva contraseña y la confirmación de la nueva contraseña coinciden
        if (!datosActualizarContrasenaUsuario.contrasena().equals(datosActualizarContrasenaUsuario.repetirContrasena())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las contraseñas no coinciden");
        }

        // Cambiar la contraseña
        usuario.cambiarContrasena(datosActualizarContrasenaUsuario, passwordEncoder);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }

    @GetMapping()
    public ResponseEntity<Page<DatosListadoUsuario>> listarUsuarios(@PageableDefault(size = 10) Pageable paginacion) {
        Page<Usuario> usuariosPaginados = usuarioRepository.findAll(paginacion);

        Page<DatosListadoUsuario> datosListado = usuariosPaginados.map(usuario -> {

            return new DatosListadoUsuario(usuario.getId(), usuario.getNombre(), usuario.getPerfil());
        });

        return ResponseEntity.ok(datosListado);
    }
}
