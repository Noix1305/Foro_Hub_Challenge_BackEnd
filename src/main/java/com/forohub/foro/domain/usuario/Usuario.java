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

@Getter
@Table(name = "usuarios")
@Entity(name = "Usuario")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @ManyToOne(optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @Column(name = "activo")
    private Boolean activo;

    public Usuario(DatosRegistroUsuario datosRegistroUsuario, PasswordEncoder passwordEncoder, Perfil perfil){
        this.nombre = datosRegistroUsuario.nombre();
        this.correoElectronico = datosRegistroUsuario.correo();
        this.contrasena = passwordEncoder.encode(datosRegistroUsuario.contrasena());
        this.perfil = perfil;
        this.activo = true;
    }

    public void actualizarCorreo(DatosActualizarCorreoUsuario datosActualizarCorreoUsuario){
        this.correoElectronico = datosActualizarCorreoUsuario.correo();
    }

    public void cambiarContrasena(DatosActualizarContrasenaUsuario datosActualizarContrasenaUsuario, PasswordEncoder passwordEncoder){
        System.out.println(this.getContrasena());
        this.contrasena = passwordEncoder.encode(datosActualizarContrasenaUsuario.contrasena());
        System.out.println(this.getContrasena());

    }



    public void inactivarUsuario(){
        this.activo = false;
    }

    public void activarUsuario(){
        this.activo = true;
    }

    @Override
    public String getUsername() {
        return correoElectronico;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
