package com.forohub.foro.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(
        @NotNull Long id,
        @NotBlank(message = "El título es obligatorio.")
        String titulo,

        @NotBlank(message = "El mensaje es obligatorio.")
        String mensaje,

        @NotNull(message = "El autor es obligatorio.")
        Long autorId,

        @NotNull(message = "El curso es obligatorio.")
        Long cursoId) {
}
