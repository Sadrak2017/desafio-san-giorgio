package br.com.uol.config;

import br.com.uol.ApplicationRest;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Calendar;
import java.util.List;

@Configuration
public class OpenAPIConfig {

  @Value("${desafio.uol-compass.ambiente.openapi.dev-url}")
  private String devUrl;
  @Value("${desafio.uol-compass.ambiente.openapi.hml-url}")
  private String hmlUrl;
  @Value("${desafio.uol-compass.ambiente.openapi.prd-url}")
  private String prodUrl;

  @Bean
  public OpenAPI openAPI() {

    Contact contact = new Contact();
    contact.setEmail("sadrak_earth@outlook.com");
    contact.setName("Uol");
    contact.setUrl("https://uol.com.br/");

    int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    License license = new License()
      .name("Uol Compass | Desafio San Giorgio")
      .url("https://exemplo.com.br/termos-e-condicoes-de-uso/");

    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("URL do servidor em ambiente de desenvolvimento");

    Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("URL do servidor em ambiente de produção");

    Server hmlServer = new Server();
    hmlServer.setUrl(hmlUrl);
    hmlServer.setDescription("URL do servidor em ambiente de homologação/poc");

    String version = "1.0.0";
    Info info = new Info()
      .title("Desafio San Giorgio - Uol Compass")
      .version(version)
      .contact(contact)
      .description("Uol Compass | Desafio San Giorgio © " + currentYear +
        " Todos os direitos reservados - Termos e condições de uso.")
      .termsOfService("https://exemplo.com.br/termos-e-condicoes-de-uso/")
      .license(license);

    ApplicationRest.setVersion(version);

    SecurityScheme basicAuthScheme = new SecurityScheme()
      .type(SecurityScheme.Type.HTTP)
      .scheme("basic");

    SecurityScheme bearerAuthScheme = new SecurityScheme()
      .type(SecurityScheme.Type.HTTP)
      .scheme("bearer")
      .bearerFormat("JWT");

    Components components = new Components()
      .addSecuritySchemes("basicAuth", basicAuthScheme)
      .addSecuritySchemes("bearerAuth", bearerAuthScheme);

    SecurityRequirement basicAuthRequirement = new SecurityRequirement().addList("Basic Auth");
    SecurityRequirement bearerAuthRequirement = new SecurityRequirement().addList("Bearer Auth");

    return new OpenAPI()
      //.components(components)
     // .addSecurityItem(basicAuthRequirement)
      .servers(List.of(devServer, hmlServer, prodServer))

      //.addSecurityItem(bearerAuthRequirement)
      .info(info);
  }

}
