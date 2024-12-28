package com.forohub.foro.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosActualizarCorreoUsuario(@NotBlank @Email String correo) {
}
