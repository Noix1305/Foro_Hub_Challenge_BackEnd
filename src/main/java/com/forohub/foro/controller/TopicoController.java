package com.forohub.foro.controller;

import com.forohub.foro.domain.curso.Curso;
import com.forohub.foro.domain.curso.CursoService;
import com.forohub.foro.domain.topico.DatosRegistroTopico;
import com.forohub.foro.domain.topico.Topico;
import com.forohub.foro.domain.topico.TopicoRepository;
import com.forohub.foro.domain.usuario.Usuario;
import com.forohub.foro.domain.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public void registrarTopico(@RequestBody @Valid DatosRegistroTopico topicoDTO) {
        // Verificar si ya existe un tópico con el mismo título y mensaje
        Topico topico = new Topico(topicoDTO);
        if (topicoRepository.existsByTituloAndMensaje(topico.getTitulo(), topico.getMensaje())) {
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje.");
        }

        // Obtener el autor y curso desde sus respectivos servicios
        Usuario autor = usuarioService.findById(topico.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Curso curso = cursoService.findById(topico.getCursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        topicoRepository.save(topico);
    }
}
