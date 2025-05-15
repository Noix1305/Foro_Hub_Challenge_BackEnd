// Paquete donde se encuentra la clase
package com.forohub.foro.infra.security;

// Importaciones necesarias para la configuración de seguridad
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Anotación que indica que esta clase contiene configuración de Spring
@Configuration
// Habilita la configuración de seguridad web
@EnableWebSecurity
public class SecurityConfiguration {

    // Inyección del filtro de seguridad personalizado que intercepta las peticiones
    @Autowired
    private SecurityFilter securityFilter;

    // Bean que define la configuración principal de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Desactiva CSRF (ya que se usa autenticación con tokens y no sesiones)
                .csrf(csrf -> csrf.disable())
                // Configuración de CORS para permitir peticiones desde un frontend en localhost:4200
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    // Permite solicitudes desde esta URL (ej. Angular en desarrollo)
                    corsConfig.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
                    // Métodos HTTP permitidos
                    corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    // Permitir todos los encabezados
                    corsConfig.setAllowedHeaders(java.util.List.of("*"));
                    // Permitir el envío de credenciales como cookies o headers de autorización
                    corsConfig.setAllowCredentials(true);
                    // Devuelve la configuración
                    return corsConfig;
                }))
                // Configura que no se usen sesiones (todo es stateless, como debe ser con JWT)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Define las rutas públicas y las que requieren autenticación
                .authorizeHttpRequests(authorize -> authorize
                        // Rutas públicas: login y registro
                        .requestMatchers(HttpMethod.POST, "/login", "/signin").permitAll()
                        // Rutas públicas: documentación Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                // Agrega el filtro personalizado antes del filtro estándar de autenticación de Spring Security
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                // Construye y devuelve la configuración
                .build();
    }

    // Bean para obtener el AuthenticationManager que maneja la autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        // Obtiene el AuthenticationManager de la configuración
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean que define el codificador de contraseñas, usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorna una instancia de codificador BCrypt (seguro y recomendado)
        return new BCryptPasswordEncoder();
    }

}
