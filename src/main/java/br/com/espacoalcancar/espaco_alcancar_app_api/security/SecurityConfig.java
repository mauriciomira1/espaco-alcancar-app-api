package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.UserService;

@Configuration
public class SecurityConfig {

  @Autowired
  private JWTProvider jwtProvider;

  @Autowired
  private UserService userService;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    SecurityFilter securityFilter = new SecurityFilter(jwtProvider, userService);
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers(HttpMethod.POST, "/user/new").permitAll()
              .requestMatchers(HttpMethod.POST, "/auth").permitAll();
          auth.anyRequest().authenticated();
        })
        .addFilterBefore(securityFilter, BasicAuthenticationFilter.class);
    return http.build();
  }

}
