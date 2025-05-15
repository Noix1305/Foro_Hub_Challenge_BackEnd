package com.forohub.foro.infra.security;

// Importa el repositorio de usuarios para acceder a datos de usuarios
import com.forohub.foro.domain.usuario.UsuarioRepository;

// Importa interfaces y excepciones de Spring Security para manejar la autenticación
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Anotación para registrar esta clase como un servicio manejado por Spring
import org.springframework.stereotype.Service;

// Marca esta clase como un servicio para la inyección de dependencias y gestión por Spring
@Service
public class AutenticacionService implements UserDetailsService {

    // Inyecta automáticamente el repositorio de usuarios para acceder a la base de datos
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método que carga los detalles de usuario por el nombre de usuario (en este caso, el correo)
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Busca un usuario en la base de datos por su correo electrónico y lo devuelve
        // Spring Security usará este UserDetails para autenticar al usuario
        return usuarioRepository.findByCorreoElectronico(correo);
        // Nota: sería buena práctica manejar el caso en que no se encuentre el usuario y lanzar UsernameNotFoundException
    }
}
