package com.forohub.foro.infra.errores;

// Importa la excepción que se lanza cuando no se encuentra una entidad en la base de datos
import jakarta.persistence.EntityNotFoundException;

// Excepción que ocurre cuando se refiere a una propiedad inválida en consultas JPA
import org.springframework.data.mapping.PropertyReferenceException;

// Clases para construir respuestas HTTP
import org.springframework.http.ResponseEntity;

// Manejo de errores de validación de argumentos
import org.springframework.validation.FieldError;

// Excepción lanzada cuando falla la validación de argumentos en un controlador REST
import org.springframework.web.bind.MethodArgumentNotValidException;

// Anotación para interceptar excepciones en controladores REST
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Anotación para manejar excepciones globalmente en controladores REST
@RestControllerAdvice
public class TratadorDeErrores {

    // Método que maneja excepciones EntityNotFoundException (cuando no se encuentra un recurso)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404() {
        // Devuelve una respuesta HTTP 404 (Not Found) sin cuerpo
        return ResponseEntity.notFound().build();
    }

    // Método que maneja excepciones de validación de datos en los argumentos del controlador
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e) {
        // Obtiene todos los errores de campo (por ejemplo, campos vacíos o inválidos)
        // y los mapea a un objeto DatosErrorValidacion para enviar detalles al cliente
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        // Devuelve una respuesta HTTP 400 (Bad Request) con la lista de errores en el cuerpo
        return ResponseEntity.badRequest().body(errores);
    }

    // Método que maneja errores relacionados con referencias a propiedades no existentes
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity tratarErrorProperty(PropertyReferenceException e){
        // Construye un mensaje con el error en mayúsculas y una descripción localizada
        var errores = e.getMessage().toUpperCase() + ": Causado por : " + e.getLocalizedMessage();
        // Devuelve una respuesta HTTP 500 (Internal Server Error) con el mensaje de error
        return ResponseEntity.internalServerError().body(errores);
    }

    // Método que maneja excepciones de argumento ilegal (argumentos inválidos o inconsistentes)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity tratarErrorIllegalArgument(IllegalArgumentException i){
        // Obtiene el mensaje de la excepción
        var errores = i.getMessage();
        // Devuelve una respuesta HTTP 400 (Bad Request) con el mensaje de error
        return ResponseEntity.badRequest().body(errores);
    }

    // Clase interna que representa el formato de cada error de validación para enviar en la respuesta
    private record DatosErrorValidacion(String campo, String error) {
        // Constructor que crea un objeto DatosErrorValidacion a partir de un FieldError
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
