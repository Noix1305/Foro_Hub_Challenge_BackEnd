package com.forohub.foro.domain.perfil;

// Importa la interfaz JpaRepository para operaciones CRUD
import org.springframework.data.jpa.repository.JpaRepository;

// Importa Optional para manejo seguro de valores que pueden estar ausentes
import java.util.Optional;

// Define la interfaz PerfilRepository que extiende JpaRepository para la entidad Perfil con clave primaria Long
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    // Método personalizado para obtener un Perfil por su id, retornando un Optional para evitar nulls
    Optional<Perfil> getPerfilById(Long aLong);
}
