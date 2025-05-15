// Paquete donde se encuentra la clase
package com.forohub.foro.infra.springDoc;

// Importaciones necesarias para definir el esquema de seguridad en Swagger/OpenAPI
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Indica que esta clase es de configuración de Spring
@Configuration
public class SpringDocConfig {

    // Define un bean que configura la instancia de OpenAPI usada por Swagger UI
    @Bean
    public OpenAPI customOpenAPI() {
        // Crea y retorna un objeto OpenAPI personalizado
        return new OpenAPI()
                // Define componentes personalizados como esquemas de seguridad
                .components(new Components()
                        // Añade un esquema de seguridad llamado "bearer-key"
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP) // Tipo de esquema: HTTP
                                        .scheme("bearer")               // Indica que se usa autenticación Bearer
                                        .bearerFormat("JWT")));         // Formato del token Bearer: JWT
    }
}
