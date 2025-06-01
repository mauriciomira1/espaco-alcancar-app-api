package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  /*
   * @Autowired
   * private JWTProvider jwtProvider;
   * 
   * @Autowired
   * private UserService userService;
   * 
   * @Autowired
   * private ProfessionalService professionalService;
   */

  @Autowired
  private FirebaseTokenFilter firebaseTokenFilter;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configure(http))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {

          auth
              .requestMatchers("/dashboard/**").authenticated()
              .requestMatchers(HttpMethod.POST, "/professional/*new").permitAll()
              .requestMatchers("/professional/**").authenticated()
              /* Requisições POST */
              .requestMatchers(HttpMethod.POST, "/user/new").permitAll()
              .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/dashboard/sp/new")
              .hasAnyRole("ADMIN", "PROFESSIONAL")

              /* Requisições PUT */
              .requestMatchers(HttpMethod.PUT, "/dashboard/sp/fillout").hasAnyRole("PATIENT")
              /* Requisições GET */
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all-of-a-professional/**")
              .hasAnyRole("ADMIN", "PROFESSIONAL")
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all").hasAnyRole("ADMIN")
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all-of-a-child/**").authenticated()
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/list-all-of-a-child").authenticated()
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/until-three-years").authenticated()
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/more-than-three-years").authenticated()
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/get-questions").authenticated()
              .requestMatchers(HttpMethod.GET, "/dashboard/sp/get-answers-by-sp-id/**")
              .hasAnyRole("ADMIN", "PROFESSIONAL")
              .requestMatchers(HttpMethod.GET, "/user/children/list").authenticated()
              .requestMatchers(HttpMethod.GET, "/user/children/list-with-data").authenticated()
              .requestMatchers(HttpMethod.GET, "/user/children/find/**").hasAnyRole("ADMIN", "PROFESSIONAL")
              .requestMatchers(HttpMethod.GET, "/user/children/list-all").hasAnyRole("ADMIN", "PROFESSIONAL")
              .requestMatchers(HttpMethod.GET, "/user/me").authenticated()
              .requestMatchers(HttpMethod.GET, "/professional/me").authenticated();
          auth.anyRequest().authenticated();
        })
        .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
