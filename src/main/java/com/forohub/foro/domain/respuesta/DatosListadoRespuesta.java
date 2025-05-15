package com.forohub.foro.domain.respuesta;

// Define un record (clase inmutable) llamado DatosListadoRespuesta
// que representa los datos que se listan para una respuesta en el sistema
public record DatosListadoRespuesta(
        Long id,             // Identificador único de la respuesta
        String mensaje,      // Contenido o mensaje de la respuesta
        String nombreAutor,  // Nombre del autor que escribió la respuesta
        String tituloTopico, // Título del tópico al que pertenece la respuesta
        String fechaCreacion // Fecha en que fue creada la respuesta, en formato String
) {
}
