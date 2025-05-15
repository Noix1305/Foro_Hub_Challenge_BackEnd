// Paquete donde se encuentra esta clase
package com.forohub.foro.infra.security;

// Importaciones necesarias para trabajar con JWT y fechas
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.forohub.foro.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Indica que esta clase es un servicio gestionado por Spring
@Service
public class TokenService {

    // Inyecta el valor de la propiedad api.security.secret desde application.properties
    @Value("${api.security.secret}")
    private String apiSecret;

    // Inyecta el valor del tiempo de expiración del token desde application.properties
    @Value("${api.security.expTime}")
    private Integer tiempoExpiracion;

    // Método que genera un token JWT para el usuario autenticado
    public String generarToken(Usuario usuario) {
        try {
            // Define el algoritmo de firma HMAC con la clave secreta
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);

            // Crea el token incluyendo:
            // - Emisor (issuer)
            // - Subject (el correo electrónico del usuario)
            // - Reclamo personalizado con el ID del usuario
            // - Fecha de expiración
            // - Firma con el algoritmo definido
            return JWT.create()
                    .withIssuer("ForuHub") // Identifica al emisor del token
                    .withSubject(usuario.getCorreoElectronico()) // Identificador del usuario
                    .withClaim("id", usuario.getId()) // Reclamo adicional con el ID
                    .withExpiresAt(generarFechaExpiracion()) // Fecha de expiración
                    .sign(algorithm); // Firma del token
        } catch (JWTCreationException exception) {
            // Si ocurre un error al generar el token, lanza una excepción genérica
            throw new RuntimeException();
        }
    }

    // Método que extrae el "subject" (correo electrónico) desde un token
    public String getSubject(String token) {
        if(token == null){
            // Si no hay token, lanza una excepción
            throw new RuntimeException();
        }

        DecodedJWT verifier = null;

        try {
            // Valida el token usando la misma clave secreta y emisor
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer("ForuHub") // Debe coincidir con el issuer usado al crear el token
                    .build()
                    .verify(token); // Verifica el token y lo decodifica

            verifier.getSubject(); // Obtiene el subject (correo) del token

        } catch (JWTVerificationException exception) {
            // Si el token no es válido (firma incorrecta, expirado, etc.), imprime el error
            System.out.println(exception.toString());
        }

        // Si el subject es nulo, significa que el token es inválido
        if (verifier.getSubject() == null) {
            throw new RuntimeException("Verifier inválido");
        }

        // Devuelve el subject (correo electrónico del usuario)
        return verifier.getSubject();
    }

    // Método que genera la fecha y hora de expiración del token
    private Instant generarFechaExpiracion() {
        // Suma los minutos de expiración configurados a la hora actual
        return LocalDateTime.now().plusMinutes(tiempoExpiracion)
                .toInstant(ZoneOffset.of("-05:00")); // Se usa la zona horaria UTC-5
    }
}
