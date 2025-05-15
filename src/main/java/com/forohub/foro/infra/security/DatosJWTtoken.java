// Define el paquete donde se encuentra esta clase
package com.forohub.foro.infra.security;

// Define un "record" de Java, que es una clase inmutable con un solo campo llamado JWTtoken
public record DatosJWTtoken(String JWTtoken) {
    // Al ser un record, automáticamente se generan:
    // - El constructor
    // - Los métodos getter (en este caso `JWTtoken()`)
    // - equals(), hashCode() y toString()
}
