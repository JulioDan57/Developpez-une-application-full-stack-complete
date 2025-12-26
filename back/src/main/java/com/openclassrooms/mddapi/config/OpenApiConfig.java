package com.openclassrooms.mddapi.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI (Swagger) de l'application.
 *
 * Cette classe configure la documentation Swagger générée via Springdoc OpenAPI.
 * Elle définit :
 *
 * <ul>
 *     <li>Les informations générales de l'API (titre, description, version)</li>
 *     <li>Les informations de contact et de licence</li>
 *     <li>Le schéma de sécurité JWT (Bearer Token)</li>
 * </ul>
 *
 * Grâce à cette configuration, Swagger UI permet d'authentifier les requêtes
 * en utilisant un jeton JWT via l'en-tête HTTP {@code Authorization}.
 *
 */
@Configuration
public class OpenApiConfig {

    /**
     * Crée et configure le bean {@link OpenAPI} utilisé par Swagger UI.
     *
     * Ce bean :
     *
     * <ul>
     *     <li>Déclare les métadonnées de l'API</li>
     *     <li>Configure la sécurité JWT avec le schéma {@code BearerAuth}</li>
     *     <li>Permet l'ajout du bouton "Authorize" dans Swagger UI</li>
     * </ul>
     *
     * @return une instance configurée de {@link OpenAPI}
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MDD API")
                        .description("API REST pou la gestion des abonnements des utilisateurs à des sujets de programmation, le partage ’articles et aux interactions via les commentaires")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Julio Daniel Gil Cano")
                                .email("daniel.gilcano@yahoo.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                // ✅ Sécurité JWT dans Swagger UI
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
