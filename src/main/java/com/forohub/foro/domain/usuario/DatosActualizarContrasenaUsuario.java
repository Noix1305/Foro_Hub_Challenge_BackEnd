package com.forohub.foro.domain.usuario;

import jakarta.validation.constraints.NotBlank; // Importa la anotación para validar que el campo no esté vacío

// Define un record (clase inmutable con campos finales) llamado DatosActualizarContrasenaUsuario
// que contiene los datos necesarios para actualizar la contraseña de un usuario
public record DatosActualizarContrasenaUsuario(
        @NotBlank String contrasenaActual,     // Contraseña actual ingresada (no puede estar vacía)
        @NotBlank String contrasena,           // Nueva contraseña que se quiere establecer (no puede estar vacía)
        @NotBlank String repetirContrasena     // Confirmación de la nueva contraseña (no puede estar vacía)
) {
}
