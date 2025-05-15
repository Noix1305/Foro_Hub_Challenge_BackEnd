package com.forohub.foro.domain.respuesta;

// Define un record llamado DatosRegistroRespuesta
// que contiene los datos necesarios para registrar una nueva respuesta
public record DatosRegistroRespuesta(
        String mensaje,   // El contenido o mensaje de la respuesta
        Long autor_id,    // ID del autor que crea la respuesta
        Long topico_id    // ID del tópico al que se asocia la respuesta
) {
}
