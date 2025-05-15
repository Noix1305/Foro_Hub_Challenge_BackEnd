package com.forohub.foro.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Define un record llamado DatosRegistroUsuario que representa
// la información necesaria para registrar un nuevo usuario
public record DatosRegistroUsuario(
        @NotBlank String nombre,       // Nombre del usuario, no puede estar vacío ni nulo
        @NotBlank String correo,       // Correo electrónico del usuario, obligatorio y no vacío
        @NotBlank String contrasena,   // Contraseña del usuario, obligatorio y no vacío
        @NotNull Long perfil_id        // Id del perfil asignado al usuario, obligatorio (no puede ser nulo)
) {
}
