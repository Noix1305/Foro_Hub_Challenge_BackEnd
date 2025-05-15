package com.forohub.foro.controller;

// Importa las clases necesarias para manejar respuestas, tópicos, usuarios y demás utilidades
import com.forohub.foro.domain.respuesta.*;
import com.forohub.foro.domain.topico.Topico;
import com.forohub.foro.domain.topico.TopicoRepository;
import com.forohub.foro.domain.usuario.Usuario;
import com.forohub.foro.domain.usuario.UsuarioRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Para requisitos de seguridad en OpenAPI (Swagger)
import jakarta.persistence.EntityNotFoundException; // Para lanzar excepciones si no se encuentran entidades
import jakarta.transaction.Transactional; // Para manejar transacciones en métodos
import jakarta.validation.Valid; // Para validar datos recibidos
import org.springframework.beans.factory.annotation.Autowired; // Para inyección de dependencias automática
import org.springframework.data.domain.Page; // Para manejo de paginación
import org.springframework.data.domain.Pageable; // Para parametrizar paginación
import org.springframework.data.web.PageableDefault; // Para configurar paginación por defecto
import org.springframework.http.HttpStatus; // Para códigos HTTP
import org.springframework.http.ResponseEntity; // Para respuestas HTTP con cuerpo y código
import org.springframework.web.bind.annotation.*; // Para anotaciones de REST controllers y endpoints

import java.time.LocalDateTime; // Para manejo de fechas y horas
import java.time.format.DateTimeFormatter; // Para formatear fechas
import java.util.Optional; // Para manejo seguro de valores que pueden ser null

@RestController // Define que esta clase es un controlador REST
@RequestMapping("/respuestas") // Mapea todas las rutas a /respuestas
@SecurityRequirement(name = "bearer-key") // Requiere autenticación por token JWT para acceder
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository; // Repositorio para manejar respuestas

    @Autowired
    private TopicoRepository topicoRepository; // Repositorio para manejar tópicos

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositorio para manejar usuarios

    // Método para registrar una nueva respuesta
    @PostMapping
    public void registrarRespuesta(@RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta) {
        // Busca el usuario autor de la respuesta por su ID, lanza excepción si no existe
        Usuario autor = usuarioRepository.findById(datosRegistroRespuesta.autor_id())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Busca el tópico relacionado por su ID, lanza excepción si no existe
        Topico topico = topicoRepository.findById(datosRegistroRespuesta.topico_id())
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));

        // Crea una instancia de Respuesta con los datos recibidos, autor y tópico asociados
        Respuesta respuesta = new Respuesta(datosRegistroRespuesta, autor, topico);

        // Guarda la respuesta en la base de datos
        respuestaRepository.save(respuesta);
    }

    // Método para obtener un listado paginado de respuestas, con filtros opcionales por autor y/o tópico
    @GetMapping
    public ResponseEntity<Page<DatosListadoRespuesta>> listadoTopicos(
            @PageableDefault(size = 10) Pageable paginacion, // Parámetros de paginación (tamaño por defecto 10)
            @RequestParam(required = false) Long autor_id,  // Parámetro opcional para filtrar por autor
            @RequestParam(required = false) Long topico_id  // Parámetro opcional para filtrar por tópico
    ) {
        Page<Respuesta> respuestasPaginadas;

        // Filtra las respuestas según los parámetros enviados
        if (autor_id != null && topico_id != null) {
            respuestasPaginadas = respuestaRepository.findByAutor_IdAndTopico_Id(autor_id, topico_id, paginacion);
        } else if (autor_id != null) {
            respuestasPaginadas = respuestaRepository.findByAutor_Id(autor_id, paginacion);
        } else if (topico_id != null) {
            respuestasPaginadas = respuestaRepository.findByTopico_Id(topico_id, paginacion);
        } else {
            respuestasPaginadas = respuestaRepository.findAll(paginacion);
        }

        // Mapea las respuestas a un DTO con datos específicos para el listado, formateando fechas y mostrando nombres
        Page<DatosListadoRespuesta> datosListado = respuestasPaginadas.map(respuesta -> {
            try {
                // Log para seguimiento
                System.out.println("Procesando Respuesta con ID: " + respuesta.getId());

                // Obtiene el nombre del autor de la respuesta
                String nombreAutor = respuesta.getAutor().getNombre();

                // Obtiene el título del tópico asociado
                String tituloTopicoFinal = respuesta.getTopico().getTitulo();

                // Obtiene y formatea la fecha de creación de la respuesta
                LocalDateTime fechaSinFormato = respuesta.getFechaCreacion();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                String fechaFormateada = fechaSinFormato.format(formatter);

                // Retorna un objeto con los datos listos para ser enviados al cliente
                return new DatosListadoRespuesta(
                        respuesta.getId(),
                        respuesta.getMensaje(),
                        nombreAutor,
                        tituloTopicoFinal,
                        fechaFormateada
                );
            } catch (Exception e) {
                // En caso de error, imprime logs y vuelve a lanzar la excepción
                System.err.println("Error al procesar la respuesta con ID: " + (respuesta != null ? respuesta.getId() : "null"));
                e.printStackTrace();
                throw e;
            }
        });

        // Retorna la página de respuestas con datos formateados y código HTTP 200 OK
        return ResponseEntity.ok(datosListado);

    }

    // Método para actualizar una respuesta existente por su ID
    @PutMapping("/{id}")
    @Transactional // Garantiza que la operación sea atómica y se guarden los cambios
    public ResponseEntity actualizarRespuesta(@PathVariable Long id, @RequestBody DatosActualizarRespuesta datosActualizarRespuesta) {
        // Busca la respuesta en la base de datos
        Optional<Respuesta> respuestaOpt = respuestaRepository.findById(id);
        if (!respuestaOpt.isPresent()) {
            // Si no existe, devuelve error 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Respuesta no encontrada");
        }
        Respuesta respuesta = respuestaOpt.get();

        // Actualiza los datos de la respuesta con la información recibida
        respuesta.actualizarDatos(datosActualizarRespuesta);

        // Guarda los cambios en la base de datos
        respuestaRepository.save(respuesta);

        // Retorna un DTO con el mensaje actualizado y código HTTP 200 OK
        return ResponseEntity.ok(new DatosActualizarRespuesta(respuesta.getMensaje()));
    }

    // Método para eliminar una respuesta por su ID
    @DeleteMapping("/{id}")
    @Transactional // Operación atómica para eliminar
    public ResponseEntity eliminarRespuesta(@PathVariable Long id) {
        // Busca la respuesta
        Optional<Respuesta> respuestaOpt = respuestaRepository.findById(id);
        if (!respuestaOpt.isPresent()) {
            // Si no existe, retorna 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Respuesta no encontrado");
        }

        // Elimina la respuesta
        respuestaRepository.deleteById(id);

        // Retorna respuesta sin contenido con código HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }

}
