package com.forohub.foro.domain.usuario;

import com.forohub.foro.domain.perfil.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "usuarios")
@Entity(name = "Usuario")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    private String contrasena;

    @ManyToOne(optional = false) // Relación con la tabla 'perfiles'
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;
}
