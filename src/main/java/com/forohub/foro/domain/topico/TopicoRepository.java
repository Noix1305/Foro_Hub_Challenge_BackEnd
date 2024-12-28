package com.forohub.foro.domain.topico;

import com.forohub.foro.domain.respuesta.Respuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    Page<Topico> findAll(Pageable paginacion);

    Page<Topico> findByCursoIdAndFechaCreacionYear(Long curso_id, Integer anio, Pageable paginacion);

    @Query("SELECT t FROM Topico t WHERE YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByFechaCreacionYear(@Param("anio") Integer anio, Pageable pageable);

    Page<Topico> findByCursoId(Long idCurso, Pageable paginacion);

    String findTituloById(Long id);
}