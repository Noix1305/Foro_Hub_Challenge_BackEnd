package com.forohub.foro.domain.respuesta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    @Query("SELECT r FROM Respuesta r WHERE r.autor.id = :autorId AND r.topico.id = :topicoId")
    Page<Respuesta> findByAutor_IdAndTopico_Id(@Param("autorId") Long autorId, @Param("topicoId") Long topicoId, Pageable pageable);

    Page<Respuesta> findByAutor_Id(Long autorId, Pageable pageable);

    Page<Respuesta> findByTopico_Id(Long topicoId, Pageable pageable);
}
