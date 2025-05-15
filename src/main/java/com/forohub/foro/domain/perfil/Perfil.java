package com.forohub.foro.domain.perfil;

// Importa las anotaciones de JPA para mapeo de entidades
import jakarta.persistence.*;

// Importa anotaciones de Lombok para generar código automáticamente
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Anotación Lombok para generar getters automáticamente para todos los campos
@Getter

// Define el nombre de la tabla en la base de datos
@Table(name = "perfiles")

// Indica que esta clase es una entidad JPA con nombre "Perfil"
@Entity(name = "Perfil")

// Lombok genera un constructor sin argumentos
@NoArgsConstructor

// Lombok genera un constructor con todos los argumentos
@AllArgsConstructor

// Lombok genera métodos equals y hashCode basados solo en el campo "id"
@EqualsAndHashCode(of = "id")
public class Perfil {

    // Define el campo "id" como clave primaria
    @Id

    // Genera el valor del id automáticamente con la estrategia de identidad (auto-increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo para almacenar el nombre del perfil
    private String nombre;
}
