package com.forohub.foro.controller;

import com.forohub.foro.domain.curso.Curso;
import com.forohub.foro.domain.curso.CursoRepository;
import com.forohub.foro.domain.topico.*;
import com.forohub.foro.domain.usuario.Usuario;
import com.forohub.foro.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository; // Inyección del repositorio para manejar datos de Topico

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositorio para manejar datos de Usuario

    @Autowired
    private CursoRepository cursoRepository; // Repositorio para manejar datos de Curso

    @PostMapping
    public void registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico) {
        // Crear una instancia de Topico a partir de los datos recibidos en el request
        Topico topico = new Topico(datosRegistroTopico);

        // Verificar si ya existe un tópico con el mismo título y mensaje para evitar duplicados
        if (topicoRepository.existsByTituloAndMensaje(topico.getTitulo(), topico.getMensaje())) {
            // Lanzar excepción si el tópico ya existe
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje.");
        }

        // Obtener el autor del tópico a partir del ID del autor
        Usuario autor = usuarioRepository.findById(topico.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Obtener el curso asociado al tópico a partir del ID del curso
        Curso curso = cursoRepository.findById(topico.getCursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        // Guardar el nuevo tópico en la base de datos
        topicoRepository.save(topico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(
            @PageableDefault(size = 10) Pageable paginacion, // Parámetros para paginación, tamaño por defecto 10
            @RequestParam(required = false) String nombreCurso, // Parámetro opcional para filtrar por nombre de curso
            @RequestParam(required = false) Integer fechaCreacion // Parámetro opcional para filtrar por año de creación
    ) {
        Page<Topico> topicosPaginados; // Página que contendrá los tópicos paginados
        Long id_curso = null; // Variable para almacenar el ID del curso si se busca por nombre

        if (nombreCurso != null) {
            // Buscar el curso por su nombre para obtener el ID
            Curso curso = cursoRepository.findByNombre(nombreCurso);
            id_curso = curso.getId();
        }

        // Decidir qué consulta realizar según los parámetros recibidos
        if (id_curso != null && fechaCreacion != null) {
            // Buscar tópicos que coincidan con curso y año
            topicosPaginados = topicoRepository.findByCursoIdAndFechaCreacionYear(id_curso, fechaCreacion, paginacion);
        } else if (nombreCurso != null) {
            // Buscar tópicos solo por curso
            topicosPaginados = topicoRepository.findByCursoId(id_curso, paginacion);
        } else if (fechaCreacion != null) {
            // Buscar tópicos solo por año
            topicosPaginados = topicoRepository.findByFechaCreacionYear(fechaCreacion, paginacion);
        } else {
            // Si no hay filtros, obtener todos los tópicos paginados
            topicosPaginados = topicoRepository.findAll(paginacion);
        }

        // Mapear cada entidad Topico a su DTO DatosListadoTopico para la respuesta
        Page<DatosListadoTopico> datosListado = topicosPaginados.map(topico -> {
            // Obtener nombre del autor por su ID
            String nombreAutor = usuarioRepository.findNombreById(topico.getAutorId());

            // Obtener nombre del curso asociado al tópico, si existe
            String nombreCursoFinal = cursoRepository.findById(topico.getCursoId())
                    .map(Curso::getNombre)
                    .orElse("Curso desconocido");

            // Obtener la fecha de creación del tópico sin formato
            LocalDateTime fechaSinFormato = topico.getFechaCreacion();

            // Definir el formato deseado para mostrar la fecha (día mes año)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

            // Formatear la fecha con el patrón definido
            String fechaFormateada = fechaSinFormato.format(formatter);

            // Construir el DTO con todos los datos para devolver en la respuesta
            return new DatosListadoTopico(
                    topico.getId(),
                    topico.getTitulo(),
                    topico.getMensaje(),
                    nombreAutor,
                    nombreCursoFinal,
                    fechaFormateada
            );
        });

        // Devolver la página de tópicos ya mapeada en el cuerpo de la respuesta HTTP
        return ResponseEntity.ok(datosListado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> obtenerTopicoPorId(@PathVariable @Valid Long id) {
        // Buscar un tópico por su ID recibido en la URL
        Optional<Topico> topicoOpt = topicoRepository.findById(id);

        // Si no existe el tópico, devolver error 404 (Not Found)
        if (!topicoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Obtener el tópico encontrado
        Topico topico = topicoOpt.get();

        // Obtener nombre del autor, o "Autor desconocido" si no existe
        String nombreAutor = usuarioRepository.findById(topico.getAutorId())
                .map(Usuario::getNombre)
                .orElse("Autor desconocido");

        // Obtener nombre del curso, o "Curso desconocido" si no existe
        String nombreCurso = cursoRepository.findById(topico.getCursoId())
                .map(Curso::getNombre)
                .orElse("Curso desconocido");

        // Obtener la fecha de creación sin formato
        LocalDateTime fechaSinFormato = topico.getFechaCreacion();

        // Definir formato de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        // Formatear la fecha
        String fechaFormateada = fechaSinFormato.format(formatter);

        // Crear objeto con los datos del tópico para la respuesta
        DatosRespuestaTopico datosTopico = new DatosRespuestaTopico(
                topico.getTitulo(),
                topico.getMensaje(),
                fechaFormateada,
                topico.getStatus(),
                nombreAutor,
                nombreCurso
        );

        // Devolver la información del tópico con código 200 OK
        return ResponseEntity.ok(datosTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizarTopico(@PathVariable Long id, @RequestBody DatosActualizarTopico datosActualizarTopico) {
        // Buscar el tópico a actualizar por ID
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if (!topicoOpt.isPresent()) {
            // Si no existe, devolver error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topico no encontrado");
        }

        // Obtener el tópico encontrado
        Topico topico = topicoOpt.get();

        // Actualizar los datos del tópico con la información recibida
        topico.actualizarDatos(datosActualizarTopico);

        // Guardar los cambios en la base de datos
        topicoRepository.save(topico);

        // Devolver la información actualizada en la respuesta
        return ResponseEntity.ok(new DatosActualizarTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getAutorId(),
                topico.getCursoId()
        ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        // Buscar tópico por ID para eliminar
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if (!topicoOpt.isPresent()) {
            // Si no se encuentra, devolver error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topico no encontrado");
        }

        // Eliminar el tópico de la base de datos
        topicoRepository.deleteById(id);

        // Devolver respuesta sin contenido (204 No Content)
        return ResponseEntity.noContent().build();
    }

}
