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

@Getter // Genera getters automáticos para todos los campos
@Table(name = "respuestas") // Define la tabla "respuestas" en la base de datos
@Entity(name = "Respuesta") // Marca esta clase como entidad JPA llamada "Respuesta"
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@EqualsAndHashCode(of = "id") // Genera métodos equals y hashCode basados en el campo "id"
public class Respuesta {

    @Id // Marca este campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera automáticamente el valor del ID
    private Long id;

    @Lob // Indica que este campo puede almacenar texto largo (Large Object)
    private String mensaje; // Contenido del mensaje de la respuesta

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    // Columna "fecha_creacion" no puede ser nula y no puede ser actualizada después de creado
    private LocalDateTime fechaCreacion; // Fecha y hora en que se creó la respuesta

    @ManyToOne(optional = false) // Relación muchos a uno con Topico, no puede ser null
    @JoinColumn(name = "topico_id", nullable = false) // Clave foránea "topico_id" en la tabla respuestas
    private Topico topico; // Tópico al que pertenece la respuesta

    @ManyToOne(optional = false) // Relación muchos a uno con Usuario, no puede ser null
    @JoinColumn(name = "autor_id", nullable = false) // Clave foránea "autor_id" en la tabla respuestas
    private Usuario autor; // Usuario que creó la respuesta

    @Column(nullable = false) // Campo obligatorio en la base de datos
    private Boolean solucion = false; // Indica si la respuesta fue marcada como solución (por defecto false)

    // Constructor que inicializa la respuesta con datos recibidos, autor y tópico relacionados
    public Respuesta(DatosRegistroRespuesta datosRegistroRespuesta, Usuario autor, Topico topico) {
        this.mensaje = datosRegistroRespuesta.mensaje(); // Mensaje de la respuesta
        this.fechaCreacion = LocalDateTime.now(); // Fecha actual al crear la respuesta
        this.autor = autor; // Autor que crea la respuesta
        this.topico = topico; // Tópico asociado a la respuesta
        this.solucion = false; // Inicialmente no es solución
    }

    // Método para actualizar el mensaje de la respuesta con nuevos datos
    public void actualizarDatos(DatosActualizarRespuesta datosActualizarRespuesta){
        this.mensaje = datosActualizarRespuesta.mensaje();
    }

    // Método para marcar esta respuesta como solución del tópico
    public void marcarSolucionado() {
        this.solucion = true;
    }
}
