package com.forohub.foro.domain.topico;

// Record que representa los datos que describen una respuesta o detalle de un tópico
public record DatosRespuestaTopico(
        String título,         // Título del tópico
        String mensaje,        // Mensaje o contenido del tópico
        String fechaDeCreación,// Fecha de creación del tópico (en formato String)
        Status estado,         // Estado del tópico (probablemente un enum que indica si está abierto, cerrado, etc.)
        String autor,          // Nombre o identificador del autor del tópico
        String curso           // Nombre o identificador del curso relacionado con el tópico
)
{
}
