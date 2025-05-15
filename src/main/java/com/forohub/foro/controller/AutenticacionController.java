package com.forohub.foro.controller;

// Importa clases para manejo de datos de autenticación, usuario y seguridad
import com.forohub.foro.domain.usuario.DatosAutenticacionUsuario;
import com.forohub.foro.domain.usuario.Usuario;
import com.forohub.foro.infra.security.DatosJWTtoken;
import com.forohub.foro.infra.security.TokenService;

import jakarta.validation.Valid;  // Para validar los datos que llegan en la petición
import org.springframework.beans.factory.annotation.Autowired;  // Para inyección automática de dependencias
import org.springframework.http.ResponseEntity;  // Para manejar las respuestas HTTP
import org.springframework.security.authentication.AuthenticationManager;  // Para manejar autenticación
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  // Token para autenticación por usuario y clave
import org.springframework.security.core.Authentication;  // Representa la autenticación en Spring Security
import org.springframework.web.bind.annotation.PostMapping;  // Para mapear peticiones POST
import org.springframework.web.bind.annotation.RequestBody;  // Para indicar que el cuerpo de la petición debe mapearse a un objeto
import org.springframework.web.bind.annotation.RequestMapping;  // Para mapear URLs a controladores
import org.springframework.web.bind.annotation.RestController;  // Marca la clase como controlador REST

@RestController  // Define esta clase como un controlador REST para manejar solicitudes HTTP
@RequestMapping("/login")  // Todas las rutas aquí serán bajo /login
public class AutenticacionController {

    @Autowired  // Inyecta automáticamente la instancia del AuthenticationManager configurado en Spring Security
    private AuthenticationManager authenticationManager;

    @Autowired  // Inyecta el servicio que genera tokens JWT
    private TokenService tokenService;

    @PostMapping  // Método que responderá a peticiones HTTP POST a /login
    public ResponseEntity autenticacionUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) {
        // Crea un token de autenticación con el login y clave proporcionados en la petición
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datosAutenticacionUsuario.login(),  // Nombre de usuario recibido
                datosAutenticacionUsuario.clave()   // Contraseña recibida
        );

        // Intenta autenticar al usuario con el token creado
        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        // (Esta línea es redundante porque se repite la autenticación, se puede eliminar)
        authenticationManager.authenticate(authToken);

        // Genera un token JWT para el usuario autenticado, obteniendo los detalles del usuario principal
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        // Retorna una respuesta HTTP 200 OK con el token JWT empaquetado en un objeto DatosJWTtoken
        return ResponseEntity.ok(new DatosJWTtoken(JWTtoken));
    }
}
