package com.forohub.foro.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u.nombre FROM Usuario u WHERE u.id = :autor_id")
    String findNombreById(@Param("autor_id") Long autor_id);

    UserDetails findByCorreoElectronico(String correo);

    boolean existsByCorreoElectronico(String correoElectronico);
}
