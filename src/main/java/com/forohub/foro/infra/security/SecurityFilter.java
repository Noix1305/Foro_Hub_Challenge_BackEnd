// Paquete donde se encuentra esta clase
package com.forohub.foro.infra.security;

// Importaciones necesarias
import com.forohub.foro.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Anotación que indica que esta clase es un componente de Spring (se puede inyectar automáticamente)
@Component
public class SecurityFilter extends OncePerRequestFilter {

    // Inyección del servicio que maneja los tokens JWT
    @Autowired
    private TokenService tokenService;

    // Inyección del repositorio de usuarios para buscar usuarios por su correo
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método que se ejecuta por cada solicitud HTTP una sola vez (por eso extiende OncePerRequestFilter)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Si la solicitud es hacia Swagger, no se realiza validación de seguridad (se omite el filtro)
        if (request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response); // Continúa con la cadena de filtros
            return; // Sale del método para no procesar más
        }

        // Obtiene el encabezado "Authorization" de la petición (donde se espera el token JWT)
        var authHeader = request.getHeader("Authorization");

        // Si el header no es nulo, intentamos procesar el token
        if (authHeader != null) {
            // Elimina el prefijo "Bearer " del token
            var token = authHeader.replace("Bearer ", "");

            // Extrae el subject (correo electrónico) desde el token
            var nombreUsuario = tokenService.getSubject(token);

            // Si el token es válido y contiene un nombre de usuario
            if (nombreUsuario != null) {
                // Busca el usuario en la base de datos
                var usuario = usuarioRepository.findByCorreoElectronico(nombreUsuario);

                // Crea un objeto de autenticación con los datos del usuario (pero sin credenciales)
                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario, // principal (el usuario)
                        null, // no se incluyen credenciales
                        usuario.getAuthorities() // roles/autorizaciones del usuario
                );

                // Establece la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continúa con la cadena de filtros (ya sea autenticado o no)
        filterChain.doFilter(request, response);
    }
}
