package com.forohub.foro.domain.curso;

// Importa la interfaz JpaRepository de Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;

// Define la interfaz CursoRepository que extiende JpaRepository para la entidad Curso con clave primaria Long
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Método personalizado para buscar un curso por su nombre
    Curso findByNombre(String nombreCurso);
}
