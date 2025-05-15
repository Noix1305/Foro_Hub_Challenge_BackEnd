package com.forohub.foro.domain.topico;

import com.forohub.foro.domain.respuesta.Respuesta; // Importa la clase Respuesta (aunque no se usa directamente aquí)
import org.springframework.data.domain.Page; // Importa para manejo de paginación
import org.springframework.data.domain.Pageable; // Importa para parámetros de paginación
import org.springframework.data.jpa.repository.JpaRepository; // Importa la interfaz base de JPA para repositorios
import org.springframework.data.jpa.repository.Query; // Importa para poder definir consultas JPQL personalizadas
import org.springframework.data.repository.query.Param; // Importa para pasar parámetros a consultas JPQL

import java.util.Optional; // Importa Optional para valores que pueden no estar presentes

// Interfaz repositorio para la entidad Topico, extiende JpaRepository para operaciones CRUD y paginación
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Método que verifica si existe un tópico con un título y mensaje específico
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    // Método que devuelve todos los tópicos paginados con la configuración pasada
    Page<Topico> findAll(Pageable paginacion);

    // Método que busca tópicos filtrando por curso (curso_id) y por año de creación (anio), paginados
    // Nota: Este método está declarado pero no tiene implementación automática estándar (requiere query personalizada)
    Page<Topico> findByCursoIdAndFechaCreacionYear(Long curso_id, Integer anio, Pageable paginacion);

    // Consulta JPQL personalizada que devuelve tópicos creados en un año específico, paginados
    @Query("SELECT t FROM Topico t WHERE YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByFechaCreacionYear(@Param("anio") Integer anio, Pageable pageable);

    // Método que busca tópicos por el id del curso, paginados
    Page<Topico> findByCursoId(Long idCurso, Pageable paginacion);

    // Método para obtener solo el título de un tópico dado su id
    String findTituloById(Long id);
}
