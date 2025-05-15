package com.forohub.foro.domain.usuario;

import com.forohub.foro.domain.perfil.Perfil;
import com.forohub.foro.domain.respuesta.DatosActualizarRespuesta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Getter // Lombok: genera getters para todos los campos
@Table(name = "usuarios") // Define la tabla "usuarios" en la base de datos
@Entity(name = "Usuario") // Marca esta clase como una entidad JPA llamada "Usuario"
@NoArgsConstructor // Lombok: genera constructor sin parámetros
@AllArgsConstructor // Lombok: genera constructor con todos los parámetros
@EqualsAndHashCode(of = "id") // Lombok: genera equals y hashCode usando solo el campo "id"

public class Usuario implements UserDetails { // La clase Usuario implementa UserDetails para integración con Spring Security

    @Id // Marca el campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación automática de ID (auto_increment)
    private Long id;

    @Column(name = "nombre", nullable = false) // Columna "nombre", no puede ser nulo
    private String nombre;

    @Column(name = "correo_electronico", unique = true, nullable = false) // Correo único y obligatorio
    private String correoElectronico;

    @Column(name = "contrasena", nullable = false) // Contraseña, campo obligatorio
    private String contrasena;

    @ManyToOne(optional = false) // Relación muchos a uno con Perfil (perfil_id), obligatorio
    @JoinColumn(name = "perfil_id", nullable = false) // Columna FK "perfil_id" no nula
    private Perfil perfil;

    @Column(name = "activo") // Columna "activo" que indica si el usuario está activo o no
    private Boolean activo;

    // Constructor para crear un Usuario desde un objeto DatosRegistroUsuario,
    // codifica la contraseña con PasswordEncoder y asigna el perfil
    public Usuario(DatosRegistroUsuario datosRegistroUsuario, PasswordEncoder passwordEncoder, Perfil perfil){
        this.nombre = datosRegistroUsuario.nombre();
        this.correoElectronico = datosRegistroUsuario.correo();
        this.contrasena = passwordEncoder.encode(datosRegistroUsuario.contrasena());
        this.perfil = perfil;
        this.activo = true; // Usuario activo por defecto al crearse
    }

    // Método para actualizar el correo electrónico del usuario
    public void actualizarCorreo(DatosActualizarCorreoUsuario datosActualizarCorreoUsuario){
        this.correoElectronico = datosActualizarCorreoUsuario.correo();
    }

    // Método para cambiar la contraseña del usuario,
    // codifica la nueva contraseña antes de guardarla
    public void cambiarContrasena(DatosActualizarContrasenaUsuario datosActualizarContrasenaUsuario, PasswordEncoder passwordEncoder){
        System.out.println(this.getContrasena()); // Imprime la contraseña actual (para debugging)
        this.contrasena = passwordEncoder.encode(datosActualizarContrasenaUsuario.contrasena()); // Codifica y actualiza
        System.out.println(this.getContrasena()); // Imprime la contraseña actualizada (para debugging)
    }

    // Método para inactivar (desactivar) al usuario
    public void inactivarUsuario(){
        this.activo = false;
    }

    // Método para activar al usuario
    public void activarUsuario(){
        this.activo = true;
    }

    // Métodos sobrescritos de UserDetails para integración con Spring Security:

    @Override
    public String getUsername() {
        return correoElectronico; // El nombre de usuario para autenticación es el correo
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve una lista con un rol fijo "ROLE_USER"
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return contrasena; // Devuelve la contraseña codificada
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta no expira (por defecto)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // La cuenta no está bloqueada (por defecto)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales no expiran (por defecto)
    }

    @Override
    public boolean isEnabled() {
        return true; // La cuenta está habilitada (por defecto)
    }
}
