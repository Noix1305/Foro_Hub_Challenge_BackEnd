package com.forohub.foro.domain.usuario;

import jakarta.validation.constraints.Email;     // Importa la anotación para validar formato de correo electrónico
import jakarta.validation.constraints.NotBlank;  // Importa la anotación para validar que el campo no esté vacío

// Define un record (clase inmutable) llamado DatosActualizarCorreoUsuario
// que contiene el dato necesario para actualizar el correo de un usuario
public record DatosActualizarCorreoUsuario(
        @NotBlank            // El correo no puede estar vacío
        @Email               // El correo debe tener formato válido de email
        String correo        // Campo para almacenar el correo electrónico nuevo
) {
}
