package com.forohub.foro.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

// Interface que extiende JpaRepository para manejar la entidad Usuario con clave primaria Long
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Consulta personalizada para obtener el nombre del usuario por su id
    @Query("SELECT u.nombre FROM Usuario u WHERE u.id = :autor_id")
    String findNombreById(@Param("autor_id") Long autor_id);

    // Método que Spring Data JPA implementa automáticamente para buscar un usuario por su correo electrónico
    // Retorna un UserDetails para integrar con Spring Security
    UserDetails findByCorreoElectronico(String correo);

    // Método que verifica si existe un usuario con un correo electrónico dado
    boolean existsByCorreoElectronico(String correoElectronico);
}
