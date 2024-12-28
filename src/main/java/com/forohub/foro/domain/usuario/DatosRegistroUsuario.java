package com.forohub.foro.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroUsuario(@NotBlank String nombre, @NotBlank String correo, @NotBlank String contrasena, @NotNull Long perfil_id) {
}
