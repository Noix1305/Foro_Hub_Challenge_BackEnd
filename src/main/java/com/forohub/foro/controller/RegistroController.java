package com.forohub.foro.controller;

// Importa las clases necesarias para manejar perfiles, usuarios, validaciones, etc.
import com.forohub.foro.domain.perfil.Perfil;
import com.forohub.foro.domain.perfil.PerfilRepository;
import com.forohub.foro.domain.usuario.DatosRegistroUsuario;
import com.forohub.foro.domain.usuario.Usuario;
import com.forohub.foro.domain.usuario.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;  // Para lanzar excepción si no se encuentra entidad
import jakarta.validation.Valid;  // Para validar datos recibidos
import org.springframework.beans.factory.annotation.Autowired;  // Para inyectar dependencias automáticamente
import org.springframework.security.crypto.password.PasswordEncoder;  // Para encriptar contraseñas
import org.springframework.web.bind.annotation.PostMapping;  // Para mapear solicitudes POST
import org.springframework.web.bind.annotation.RequestBody;  // Para recibir datos JSON en el cuerpo de la petición
import org.springframework.web.bind.annotation.RequestMapping;  // Para mapear rutas base del controlador
import org.springframework.web.bind.annotation.RestController;  // Marca esta clase como controlador REST

import java.util.Optional;

@RestController  // Define que esta clase manejará peticiones REST
@RequestMapping("/signin")  // Todas las rutas en esta clase serán bajo /signin
public class RegistroController {

    @Autowired  // Inyecta automáticamente el repositorio de usuarios para consultas y guardado
    UsuarioRepository usuarioRepository;

    @Autowired  // Inyecta el repositorio de perfiles para consultar perfiles en la base de datos
    PerfilRepository perfilRepository;

    @Autowired  // Inyecta el codificador de contraseñas para encriptar las claves antes de guardarlas
    private PasswordEncoder passwordEncoder;

    @PostMapping  // Este método responderá a peticiones HTTP POST a /signin
    public void registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario) {
        // Busca un perfil en base al id enviado en los datos del registro, o lanza error si no existe
        Perfil peril = perfilRepository.findById(datosRegistroUsuario.perfil_id())
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado"));

        // Crea una nueva instancia de Usuario con los datos recibidos, el password encoder y el perfil encontrado
        Usuario usuario = new Usuario(datosRegistroUsuario, passwordEncoder, peril);

        // Verifica si ya existe un usuario con el mismo correo electrónico para evitar duplicados
        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            // Si ya existe un usuario con ese correo, lanza una excepción con mensaje claro
            throw new IllegalArgumentException("Ya existe un usuario registrado con el mismo correo electronico.");
        }

        // Guarda el usuario en la base de datos
        usuarioRepository.save(usuario);
    }
}
