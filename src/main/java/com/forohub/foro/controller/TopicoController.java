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
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public void registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico) {
        // Verificar si ya existe un tópico con el mismo título y mensaje
        Topico topico = new Topico(datosRegistroTopico);
        if (topicoRepository.existsByTituloAndMensaje(topico.getTitulo(), topico.getMensaje())) {
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje.");
        }

        // Obtener el autor y curso desde sus respectivos servicios
        Usuario autor = usuarioRepository.findById(topico.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Curso curso = cursoRepository.findById(topico.getCursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        topicoRepository.save(topico);
    }

//    @PostMapping
//    public ResponseEntity<Void> registrarTopicos(@RequestBody @Valid List<DatosRegistroTopico> topicoDTOs) {
//        topicoDTOs.forEach(topicoDTO -> {
//            Topico topico = new Topico(topicoDTO);
//            if (topicoRepository.existsByTituloAndMensaje(topico.getTitulo(), topico.getMensaje())) {
//                throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje.");
//            }
//            topicoRepository.save(topico);
//        });
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(
            @PageableDefault(size = 10) Pageable paginacion,
            @RequestParam(required = false) String nombreCurso, // Parámetro de búsqueda por nombre de curso
            @RequestParam(required = false) Integer fechaCreacion // Parámetro de búsqueda por año
    ) {
        Page<Topico> topicosPaginados;
        Long id_curso = null;

        if (nombreCurso != null) {
            Curso curso = cursoRepository.findByNombre(nombreCurso);
            id_curso = curso.getId();
        }

        // Verificar los parámetros de búsqueda
        if (id_curso != null && fechaCreacion != null) {
            // Buscar por nombre de curso y año
            topicosPaginados = topicoRepository.findByCursoIdAndFechaCreacionYear(id_curso, fechaCreacion, paginacion);
        } else if (nombreCurso != null) {
            // Buscar solo por nombre de curso
            topicosPaginados = topicoRepository.findByCursoId(id_curso, paginacion);
        } else if (fechaCreacion != null) {
            // Buscar solo por año
            topicosPaginados = topicoRepository.findByFechaCreacionYear(fechaCreacion, paginacion);
        } else {
            // Si no hay criterios, retornar todos los tópicos
            topicosPaginados = topicoRepository.findAll(paginacion);
        }

        // Mapear cada tópico a DatosListadoTopico
        Page<DatosListadoTopico> datosListado = topicosPaginados.map(topico -> {
            String nombreAutor = usuarioRepository.findNombreById(topico.getAutorId());

            String nombreCursoFinal = cursoRepository.findById(topico.getCursoId())
                    .map(Curso::getNombre)
                    .orElse("Curso desconocido");
            LocalDateTime fechaSinFormato = topico.getFechaCreacion();

            // Definir el formato de la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

            // Formatear la fecha
            String fechaFormateada = fechaSinFormato.format(formatter);

            return new DatosListadoTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), nombreAutor, nombreCursoFinal, fechaFormateada);
        });

        return ResponseEntity.ok(datosListado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> obtenerTopicoPorId(@PathVariable @Valid Long id) {
        // Buscar el tópico por el ID
        Optional<Topico> topicoOpt = topicoRepository.findById(id);

        // Verificar si el tópico existe
        if (!topicoOpt.isPresent()) {
            // Si no existe, devolver un error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Obtener el tópico
        Topico topico = topicoOpt.get();

        // Obtener el nombre del autor
        String nombreAutor = usuarioRepository.findById(topico.getAutorId())
                .map(Usuario::getNombre)
                .orElse("Autor desconocido");

        // Obtener el nombre del curso
        String nombreCurso = cursoRepository.findById(topico.getCursoId())
                .map(Curso::getNombre)
                .orElse("Curso desconocido");

        LocalDateTime fechaSinFormato = topico.getFechaCreacion();

        // Definir el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        // Formatear la fecha
        String fechaFormateada = fechaSinFormato.format(formatter);

        // Crear y retornar la respuesta con los datos del tópico
        DatosRespuestaTopico datosTopico = new DatosRespuestaTopico(
                topico.getTitulo(),
                topico.getMensaje(),
                fechaFormateada,
                topico.getStatus(),
                nombreAutor,
                nombreCurso
        );

        return ResponseEntity.ok(datosTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizarTopico(@PathVariable Long id, @RequestBody DatosActualizarTopico datosActualizarTopico) {
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if (!topicoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topico no encontrado");
        }
        Topico topico = topicoOpt.get();
        topico.actualizarDatos(datosActualizarTopico);
        topicoRepository.save(topico);
        return ResponseEntity.ok(new DatosActualizarTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getAutorId(), topico.getCursoId()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        Optional<Topico> topicoOpt = topicoRepository.findById(id);
        if (!topicoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topico no encontrado");
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
