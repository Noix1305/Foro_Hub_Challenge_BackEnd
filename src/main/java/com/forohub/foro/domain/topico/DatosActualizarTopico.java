package com.forohub.foro.domain.topico;

import jakarta.validation.constraints.NotBlank; // Importa la anotación para validar que un campo no sea vacío o nulo
import jakarta.validation.constraints.NotNull;  // Importa la anotación para validar que un campo no sea nulo

// Record que representa los datos necesarios para actualizar un tópico
public record DatosActualizarTopico(

        @NotNull // Valida que el id no sea nulo
        Long id,

        @NotBlank(message = "El título es obligatorio.") // Valida que el título no sea nulo ni vacío, con mensaje personalizado
        String titulo,

        @NotBlank(message = "El mensaje es obligatorio.") // Valida que el mensaje no sea nulo ni vacío, con mensaje personalizado
        String mensaje,

        @NotNull(message = "El autor es obligatorio.") // Valida que el id del autor no sea nulo, con mensaje personalizado
        Long autorId,

        @NotNull(message = "El curso es obligatorio.") // Valida que el id del curso no sea nulo, con mensaje personalizado
        Long cursoId) {
}
