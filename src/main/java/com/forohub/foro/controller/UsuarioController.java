package com.forohub.foro.controller;

// Importación de clases necesarias para manejar cursos, tópicos, usuarios, seguridad, paginación, etc.
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

// Define que esta clase es un controlador REST que manejará solicitudes HTTP para la ruta "/usuarios"
@RestController
@RequestMapping("/usuarios")

// Requiere que para acceder a sus métodos se use seguridad con bearer token (JWT u otro)
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    // Inyección automática del repositorio para manejar datos de usuarios
    @Autowired
    UsuarioRepository usuarioRepository;

    // Inyección automática del codificador de contraseñas para validar y encriptar
    @Autowired
    PasswordEncoder passwordEncoder;

    // Método para desactivar (inactivar) un usuario por su ID, recibe el ID por la URL
    @PutMapping("/desativar/{id}")
    @Transactional // Marca este método para que la transacción con la base de datos sea automática
    public ResponseEntity desactivarUsuario(@PathVariable Long id) {

        Usuario usuario = null;

        // Buscar el usuario en la base de datos por ID, puede o no existir
        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);

        // Si el usuario no existe, responder con código 404 (No encontrado) y mensaje
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            // Si existe, obtener el objeto usuario
            usuario = usuarioOPT.get();
        }

        // Cambiar estado del usuario a inactivo
        usuario.inactivarUsuario();

        // Responder con código 204 (sin contenido) indicando que la operación fue exitosa
        return ResponseEntity.noContent().build();
    }

    // Método para activar un usuario por su ID, similar al anterior
    @PutMapping("/activar/{id}")
    @Transactional
    public ResponseEntity activarUsuario(@PathVariable Long id) {

        Usuario usuario = null;

        // Buscar usuario por ID
        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);

        // Si no existe, devolver error 404
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            // Si existe, obtener el usuario
            usuario = usuarioOPT.get();
        }

        // Cambiar estado del usuario a activo
        usuario.activarUsuario();

        // Responder con 204 No Content para indicar éxito
        return ResponseEntity.noContent().build();
    }

    // Método para actualizar el correo electrónico de un usuario por su ID
    @PutMapping("/actualizaCorreo/{id}")
    @Transactional
    public ResponseEntity cambiarCorreo(@PathVariable Long id, @RequestBody DatosActualizarCorreoUsuario datosActualizarCorreoUsuario) {

        Usuario usuario = null;

        // Buscar usuario por ID
        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);

        // Si no existe, responder 404
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            // Si existe, obtener el usuario
            usuario = usuarioOPT.get();
        }

        // Actualizar el correo del usuario con los datos recibidos en el cuerpo del request
        usuario.actualizarCorreo(datosActualizarCorreoUsuario);

        // Guardar los cambios en la base de datos
        usuarioRepository.save(usuario);

        // Devolver la nueva información de correo actualizada
        return ResponseEntity.ok(new DatosActualizarCorreoUsuario(usuario.getCorreoElectronico()));
    }

    // Método para cambiar la contraseña de un usuario, recibe ID y datos de contraseña en el request body
    @PutMapping("/cambiarContrasena/{id}")
    @Transactional
    public ResponseEntity cambiarContrasena(@PathVariable Long id, @RequestBody DatosActualizarContrasenaUsuario datosActualizarContrasenaUsuario) {

        Usuario usuario = null;

        // Buscar usuario por ID
        Optional<Usuario> usuarioOPT = usuarioRepository.findById(id);

        // Si no existe, devolver 404
        if (!usuarioOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } else {
            // Si existe, obtener usuario
            usuario = usuarioOPT.get();
        }

        // Obtener la contraseña actual que ingresó el usuario desde el request
        String contrasenaActualIngresada = datosActualizarContrasenaUsuario.contrasenaActual();

        // Validar que la contraseña ingresada coincide con la almacenada en la base de datos
        if (!passwordEncoder.matches(contrasenaActualIngresada, usuario.getContrasena())) {
            // Si no coinciden, responder con error 400 (Bad Request) y mensaje
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contraseña actual incorrecta");
        }

        // Validar que la nueva contraseña y la confirmación coincidan
        if (!datosActualizarContrasenaUsuario.contrasena().equals(datosActualizarContrasenaUsuario.repetirContrasena())) {
            // Si no coinciden, responder con error 400 y mensaje
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las contraseñas no coinciden");
        }

        // Cambiar la contraseña usando método del usuario y el encoder para encriptarla
        usuario.cambiarContrasena(datosActualizarContrasenaUsuario, passwordEncoder);

        // Guardar los cambios en la base de datos
        usuarioRepository.save(usuario);

        // Responder con mensaje de éxito
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }

    // Método para listar usuarios con paginación, tamaño por defecto 10
    @GetMapping()
    public ResponseEntity<Page<DatosListadoUsuario>> listarUsuarios(@PageableDefault(size = 10) Pageable paginacion) {

        // Obtener página de usuarios desde la base de datos
        Page<Usuario> usuariosPaginados = usuarioRepository.findAll(paginacion);

        // Mapear cada usuario a su DTO DatosListadoUsuario para la respuesta
        Page<DatosListadoUsuario> datosListado = usuariosPaginados.map(usuario -> {
            // Crear y devolver nuevo DTO con id, nombre y perfil de usuario
            return new DatosListadoUsuario(usuario.getId(), usuario.getNombre(), usuario.getPerfil());
        });

        // Devolver la página de usuarios en la respuesta HTTP
        return ResponseEntity.ok(datosListado);
    }
}
