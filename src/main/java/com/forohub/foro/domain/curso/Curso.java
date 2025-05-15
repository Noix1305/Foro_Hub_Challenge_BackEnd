package com.forohub.foro.domain.curso;

// Importa las anotaciones y clases necesarias para usar JPA y Lombok
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Genera automáticamente los métodos getters para todos los atributos
@Getter

// Define que esta clase representa una tabla llamada "cursos" en la base de datos
@Table(name = "cursos")

// Marca esta clase como una entidad JPA con el nombre "Curso"
@Entity(name = "Curso")

// Genera un constructor sin argumentos (por defecto)
@NoArgsConstructor

// Genera un constructor con todos los argumentos (todos los atributos)
@AllArgsConstructor

// Genera métodos equals() y hashCode() considerando solo el atributo "id" para comparar objetos
@EqualsAndHashCode(of = "id")
public class Curso {

    // Define la clave primaria de la entidad
    @Id

    // Define la estrategia para autogenerar el valor de la clave primaria (auto incremental)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador único del curso

    private String nombre;  // Nombre del curso

    private String categoria;  // Categoría a la que pertenece el curso
}
