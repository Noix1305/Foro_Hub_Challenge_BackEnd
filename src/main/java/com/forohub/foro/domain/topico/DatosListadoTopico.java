package com.forohub.foro.domain.topico;

import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas (aunque no se usa directamente en este record)

// Record que representa los datos para listar un tópico, incluyendo su información básica y detalles relacionados
public record DatosListadoTopico(
        Long id,              // ID del tópico
        String titulo,        // Título del tópico
        String mensaje,       // Mensaje o contenido del tópico
        String nombreAutor,   // Nombre del autor que creó el tópico
        String nombreCurso,   // Nombre del curso al que pertenece el tópico
        String fechaCreacion  // Fecha de creación formateada como String
) {
}
