package com.forohub.foro.domain.respuesta;

import com.forohub.foro.domain.topico.DatosRegistroTopico;
import com.forohub.foro.domain.topico.Status;
import com.forohub.foro.domain.topico.Topico;
import com.forohub.foro.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Table(name = "respuestas")
@Entity(name = "Respuesta")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(optional = false) // Relación con Topico (topico_id)
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @ManyToOne(optional = false) // Relación con Usuario (autor_id)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false)
    private Boolean solucion = false; // Valor por defecto en la base de datos

    public Respuesta(DatosRegistroRespuesta datosRegistroRespuesta, Usuario autor, Topico topico) {
        this.mensaje = datosRegistroRespuesta.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.autor = autor;
        this.topico = topico;
        this.solucion = false;

    }

    public void actualizarDatos(DatosActualizarRespuesta datosActualizarRespuesta){
        this.mensaje = datosActualizarRespuesta.mensaje();
    }

    public void marcarSolucionado() {
        this.solucion = true;
    }

}
