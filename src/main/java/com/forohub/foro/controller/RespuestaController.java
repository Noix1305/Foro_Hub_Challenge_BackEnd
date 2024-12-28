package com.forohub.foro.controller;

import com.forohub.foro.domain.respuesta.*;
import com.forohub.foro.domain.topico.Topico;
import com.forohub.foro.domain.topico.TopicoRepository;
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
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public void registrarRespuesta(@RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta) {

        Usuario autor = usuarioRepository.findById(datosRegistroRespuesta.autor_id())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Topico topico = topicoRepository.findById(datosRegistroRespuesta.topico_id())
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
        Respuesta respuesta = new Respuesta(datosRegistroRespuesta, autor, topico);
        // Obtener el autor y topico desde sus respectivos servicios


        respuestaRepository.save(respuesta);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoRespuesta>> listadoTopicos(
            @PageableDefault(size = 10) Pageable paginacion,
            @RequestParam(required = false) Long autor_id,
            @RequestParam(required = false) Long topico_id
    ) {
        Page<Respuesta> respuestasPaginadas;

        if (autor_id != null && topico_id != null) {
            respuestasPaginadas = respuestaRepository.findByAutor_IdAndTopico_Id(autor_id, topico_id, paginacion);
        } else if (autor_id != null) {
            respuestasPaginadas = respuestaRepository.findByAutor_Id(autor_id, paginacion);
        } else if (topico_id != null) {
            respuestasPaginadas = respuestaRepository.findByTopico_Id(topico_id, paginacion);
        } else {
            respuestasPaginadas = respuestaRepository.findAll(paginacion);
        }

        Page<DatosListadoRespuesta> datosListado = respuestasPaginadas.map(respuesta -> {
            try {
                // Log del ID de la respuesta
                System.out.println("Procesando Respuesta con ID: " + respuesta.getId());

                // Obtener nombre del autor
                String nombreAutor = respuesta.getAutor().getNombre(); // Ajusta según tu entidad Usuario
                System.out.println("Nombre del autor: " + nombreAutor);

                // Obtener título del tópico
                String tituloTopicoFinal = respuesta.getTopico().getTitulo(); // Ajusta según tu entidad Topico
                System.out.println("Título del tópico: " + tituloTopicoFinal);

                // Formatear la fecha
                LocalDateTime fechaSinFormato = respuesta.getFechaCreacion();
                System.out.println("Fecha sin formato: " + fechaSinFormato);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                String fechaFormateada = fechaSinFormato.format(formatter);
                System.out.println("Fecha formateada: " + fechaFormateada);

                return new DatosListadoRespuesta(
                        respuesta.getId(),
                        respuesta.getMensaje(),
                        nombreAutor,
                        tituloTopicoFinal,
                        fechaFormateada
                );
            } catch (Exception e) {
                // Log del error
                System.err.println("Error al procesar la respuesta con ID: " + (respuesta != null ? respuesta.getId() : "null"));
                e.printStackTrace();
                throw e; // Vuelve a lanzar el error para que se registre en los logs del sistema
            }
        });

        return ResponseEntity.ok(datosListado);

    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizarRespuesta(@PathVariable Long id, @RequestBody DatosActualizarRespuesta datosActualizarRespuesta) {
        Optional<Respuesta> respuestaOpt = respuestaRepository.findById(id);
        if (!respuestaOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Respuesta no encontrada");
        }
        Respuesta respuesta = respuestaOpt.get();
        respuesta.actualizarDatos(datosActualizarRespuesta);
        respuestaRepository.save(respuesta);
        return ResponseEntity.ok(new DatosActualizarRespuesta(respuesta.getMensaje()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarRespuesta(@PathVariable Long id) {
        Optional<Respuesta> respuestaOpt = respuestaRepository.findById(id);
        if (!respuestaOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Respuesta no encontrado");
        }
        respuestaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
