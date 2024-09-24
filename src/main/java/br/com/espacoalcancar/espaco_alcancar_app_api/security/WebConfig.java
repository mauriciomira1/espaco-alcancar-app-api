package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
    registry.addMapping("/**") // Permite todas as rotas
        .allowedOrigins("http://localhost:3000") // Permite requisições deste domínio
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
        .allowedHeaders("*") // Permite todos os headers
        .allowCredentials(true); // Permite envio de cookies
  }
}
