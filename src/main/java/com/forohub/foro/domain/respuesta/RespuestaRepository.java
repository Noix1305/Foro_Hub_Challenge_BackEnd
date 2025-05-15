package com.forohub.foro.domain.respuesta;

import org.springframework.data.domain.Page; // Importa la clase Page para paginación
import org.springframework.data.domain.Pageable; // Importa la interfaz Pageable para manejar paginación
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository para acceso a datos JPA
import org.springframework.data.jpa.repository.Query; // Importa Query para consultas personalizadas
import org.springframework.data.repository.query.Param; // Importa Param para pasar parámetros en consultas

// Interfaz que extiende JpaRepository para operaciones CRUD y consultas en la entidad Respuesta
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    // Consulta personalizada con JPQL para buscar respuestas por autor y tópico con paginación
    @Query("SELECT r FROM Respuesta r WHERE r.autor.id = :autorId AND r.topico.id = :topicoId")
    Page<Respuesta> findByAutor_IdAndTopico_Id(
            @Param("autorId") Long autorId, // Parámetro para el id del autor
            @Param("topicoId") Long topicoId, // Parámetro para el id del tópico
            Pageable pageable); // Parámetro para paginación

    // Método generado automáticamente por Spring Data JPA para buscar respuestas por id del autor con paginación
    Page<Respuesta> findByAutor_Id(Long autorId, Pageable pageable);

    // Método generado automáticamente por Spring Data JPA para buscar respuestas por id del tópico con paginación
    Page<Respuesta> findByTopico_Id(Long topicoId, Pageable pageable);
}
