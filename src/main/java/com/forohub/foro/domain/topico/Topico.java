package com.forohub.foro.domain.topico;

import com.forohub.foro.domain.curso.Curso;  // Importa la clase Curso (aunque no se usa directamente en este archivo)
import com.forohub.foro.domain.usuario.Usuario; // Importa la clase Usuario (aunque no se usa directamente aquí)
import com.forohub.foro.domain.respuesta.Respuesta; // Importa la clase Respuesta, relacionada con Topico
import jakarta.persistence.*; // Importa las anotaciones y clases necesarias para JPA (persistencia)
import lombok.*; // Importa anotaciones de Lombok para generar código automático (getters, constructores, etc)

import java.time.LocalDateTime; // Importa la clase para manejar fechas y horas
import java.util.List; // Importa List para manejar colecciones

@Getter // Genera automáticamente los métodos get para todos los atributos
@Table(name = "topicos") // Define el nombre de la tabla en la base de datos
@Entity(name = "Topico") // Marca esta clase como entidad JPA con nombre "Topico"
@NoArgsConstructor // Genera un constructor vacío (sin argumentos)
@AllArgsConstructor // Genera un constructor con todos los argumentos (para todos los campos)
@EqualsAndHashCode(of = "id") // Genera métodos equals y hashCode basados solo en el campo 'id'
public class Topico {

    @Id // Marca este campo como clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor del id será generado automáticamente por la base de datos
    private Long id;

    private String titulo; // Título del tópico
    private String mensaje; // Mensaje o contenido del tópico

    @Column(name = "fecha_creacion", nullable = false, updatable = false) // Columna para la fecha de creación, no puede ser nula y no se actualiza después
    private LocalDateTime fechaCreacion; // Fecha y hora en que se creó el tópico

    @Enumerated(EnumType.STRING) // Almacena el enum Status como cadena en la base de datos
    @Column(nullable = false) // Esta columna no puede ser nula
    private Status status; // Estado del tópico (ABIERTO o CERRADO)

    private Long autorId; // ID del usuario que creó el tópico
    private Long cursoId; // ID del curso al que pertenece el tópico

    // Relación uno a muchos con Respuesta: un tópico puede tener muchas respuestas
    // mappedBy indica que la relación es controlada desde la entidad Respuesta en el atributo "topico"
    // CascadeType.ALL propaga todas las operaciones (persistir, eliminar, actualizar) a las respuestas
    // FetchType.LAZY carga las respuestas solo cuando se necesitan (optimización)
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas;

    // Constructor personalizado para crear un tópico a partir de los datos recibidos
    public Topico(DatosRegistroTopico datosRegistroTopico) {
        this.titulo = datosRegistroTopico.titulo(); // Asigna el título
        this.mensaje = datosRegistroTopico.mensaje(); // Asigna el mensaje
        this.fechaCreacion = LocalDateTime.now(); // Guarda la fecha/hora actual de creación
        this.status = Status.ABIERTO; // Por defecto, el tópico se crea en estado ABIERTO
        this.autorId = datosRegistroTopico.autorId(); // Asigna el ID del autor
        this.cursoId = datosRegistroTopico.cursoId(); // Asigna el ID del curso
    }

    // Método para actualizar el título y mensaje del tópico si se reciben valores no nulos
    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico) {
        if (datosActualizarTopico.titulo() != null) {
            this.titulo = datosActualizarTopico.titulo(); // Actualiza el título
        }

        if (datosActualizarTopico.mensaje() != null) {
            this.mensaje = datosActualizarTopico.mensaje(); // Actualiza el mensaje
        }
    }

    // Método para cambiar el estado del tópico a CERRADO
    public void cerrarTopico() {
        this.status = Status.CERRADO;
    }
}
