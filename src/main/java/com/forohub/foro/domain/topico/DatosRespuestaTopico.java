package com.forohub.foro.domain.topico;

public record DatosRespuestaTopico(
        String título, String mensaje, String fechaDeCreación, Status estado, String autor, String curso
)
{
}
