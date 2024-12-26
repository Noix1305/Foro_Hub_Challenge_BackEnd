package com.forohub.foro.domain.usuario;

import com.forohub.foro.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u.nombre FROM Usuario u WHERE u.id = :autor_id")
    String findNombreById(@Param("autor_id") Long autor_id);

}
