package com.forohub.foro.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopico(Long id, String titulo, String mensaje, String nombreAutor, String nombreCurso, String fechaCreacion) {

}
