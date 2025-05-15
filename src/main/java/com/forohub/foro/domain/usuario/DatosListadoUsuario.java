package com.forohub.foro.domain.usuario;

import com.forohub.foro.domain.perfil.Perfil;

// Define un record llamado DatosListadoUsuario
// que encapsula la información básica que se muestra en una lista de usuarios
public record DatosListadoUsuario(
        Long id,        // Identificador único del usuario
        String nombre,  // Nombre del usuario
        Perfil perfil   // Perfil asociado al usuario (relación con la entidad Perfil)
) {
}
