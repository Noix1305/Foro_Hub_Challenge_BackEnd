package com.forohub.foro.domain.usuario;

import com.forohub.foro.domain.perfil.Perfil;

public record DatosListadoUsuario(Long id, String nombre, Perfil perfil) {
}
