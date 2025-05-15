package com.forohub.foro.domain.respuesta;

// Importa la anotación para validar que un campo no esté vacío o en blanco
import jakarta.validation.constraints.NotBlank;

// Define un record (una clase inmutable) llamado DatosActualizarRespuesta
// que tiene un solo campo 'mensaje' que no puede estar en blanco
public record DatosActualizarRespuesta (
        @NotBlank String mensaje  // El mensaje de la respuesta que se quiere actualizar, obligatorio y no vacío
) {
}
