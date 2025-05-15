package com.forohub.foro.domain.topico;

import jakarta.validation.constraints.NotBlank; // Para validar que una cadena no esté vacía ni nula
import jakarta.validation.constraints.NotNull;  // Para validar que un objeto no sea nulo

import java.time.LocalDateTime; // Importado pero no usado en este record

// Record que representa los datos necesarios para registrar un nuevo tópico
public record DatosRegistroTopico(

        @NotBlank(message = "El título es obligatorio.") // Validación: el título no puede estar vacío ni ser nulo
        String titulo,                                   // Título del tópico a registrar

        @NotBlank(message = "El mensaje es obligatorio.") // Validación: el mensaje no puede estar vacío ni ser nulo
        String mensaje,                                    // Mensaje o contenido del tópico

        @NotNull(message = "El autor es obligatorio.")    // Validación: el ID del autor no puede ser nulo
        Long autorId,                                      // ID del usuario que crea el tópico

        @NotNull(message = "El curso es obligatorio.")    // Validación: el ID del curso no puede ser nulo
        Long cursoId                                       // ID del curso asociado al tópico

) {}
