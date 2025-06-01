package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private FirebaseTokenFilter firebaseTokenFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Configuração de CORS
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        // Desabilita CSRF (se não for usar formulários tradicionais)
        .csrf(csrf -> csrf.disable())

        // Definição de regras de autorização
        .authorizeHttpRequests(auth -> auth
            // Rotas que iniciam/retornam do fluxo OAuth2 devem ser abertas:
            .requestMatchers("/oauth2/authorization/**").permitAll()
            .requestMatchers("/login/oauth2/code/**").permitAll()

            // Outros endpoints públicos:
            .requestMatchers(HttpMethod.POST, "/user/new").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/professional/*new").permitAll()

            // Endpoints que exigem papéis específicos:
            .requestMatchers(HttpMethod.POST, "/dashboard/sp/new")
            .hasAnyRole("ADMIN", "PROFESSIONAL")
            .requestMatchers(HttpMethod.PUT, "/dashboard/sp/fillout")
            .hasAnyRole("PATIENT")
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all-of-a-professional/**")
            .hasAnyRole("ADMIN", "PROFESSIONAL")
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all").hasAnyRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/get-answers-by-sp-id/**")
            .hasAnyRole("ADMIN", "PROFESSIONAL")
            .requestMatchers(HttpMethod.GET, "/user/children/find/**")
            .hasAnyRole("ADMIN", "PROFESSIONAL")
            .requestMatchers(HttpMethod.GET, "/user/children/list-all")
            .hasAnyRole("ADMIN", "PROFESSIONAL")

            // Endpoints que exigem autenticação (qualquer usuário logado):
            .requestMatchers("/dashboard/**").authenticated()
            .requestMatchers("/professional/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all-of-a-child/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all-of-a-child").authenticated()
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/until-three-years").authenticated()
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/more-than-three-years").authenticated()
            .requestMatchers(HttpMethod.GET, "/dashboard/sp/get-questions").authenticated()
            .requestMatchers(HttpMethod.GET, "/user/children/list").authenticated()
            .requestMatchers(HttpMethod.GET, "/user/children/list-with-data").authenticated()
            .requestMatchers(HttpMethod.GET, "/user/me").authenticated()
            .requestMatchers(HttpMethod.GET, "/professional/me").authenticated()

            // Qualquer outra rota não listada acima deve exigir autenticação:
            .anyRequest().authenticated())

        // Configuração do OAuth2 Login
        .oauth2Login(oauth2 -> oauth2
            // Para onde redirecionar após login bem-sucedido:
            .defaultSuccessUrl("http://localhost:3000/dashboard", true))

        // Filtro customizado do Firebase (antes do filtro de Username/Password)
        .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
