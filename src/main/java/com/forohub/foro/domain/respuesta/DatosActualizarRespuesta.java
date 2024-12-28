package com.forohub.foro.domain.respuesta;

import jakarta.validation.constraints.NotBlank;

public record DatosActualizarRespuesta (@NotBlank String mensaje) {
}
