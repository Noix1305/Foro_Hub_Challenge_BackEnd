package com.forohub.foro.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosActualizarContrasenaUsuario(@NotBlank String contrasenaActual,@NotBlank String contrasena, @NotBlank String repetirContrasena) {
}
