package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JWTProvider jwtProvider;

  @Autowired
  private UserService userService;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    SecurityFilter securityFilter = new SecurityFilter(jwtProvider, userService);
    http
        .cors(cors -> cors.configure(http))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {

          /* Requisições POST */
          auth.requestMatchers(HttpMethod.POST, "/user/new").permitAll()
              .requestMatchers(HttpMethod.POST, "/auth").permitAll()
              .requestMatchers("/dashboard/**").authenticated()

              /* Requisições GET */
              .requestMatchers(HttpMethod.GET, "/user/children/list").authenticated()
              .requestMatchers(HttpMethod.GET, "/user/children/list-all").hasAnyRole("ADMIN", "PROFESSIONAL")
              .requestMatchers(HttpMethod.GET, "/user/me").authenticated();
          auth.anyRequest().authenticated();
        })
        .addFilterBefore(securityFilter, BasicAuthenticationFilter.class);
    return http.build();
  }
}
