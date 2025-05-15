package com.forohub.foro.domain.usuario;

// Define un record llamado DatosAutenticacionUsuario
// que contiene los datos necesarios para la autenticación de un usuario
public record DatosAutenticacionUsuario(
        String login,  // Campo para el nombre de usuario o login
        String clave   // Campo para la contraseña o clave del usuario
) {
}
