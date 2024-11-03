package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserDashboardResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SecurityFilter extends OncePerRequestFilter {

  private final JWTProvider jwtProvider;
  private final UserService userService;

  SecurityFilter(JWTProvider jwtProvider, UserService userService) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    // Removendo qualquer autorização que exista
    SecurityContextHolder.getContext().setAuthentication(null);

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      var subjectToken = jwtProvider.validateToken(token);

      if (subjectToken.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Token is empty or invalid");
        return;
      }

      // Convertendo o subjectToken (ID do usuário) para Integer
      Integer userId;
      try {
        userId = Integer.parseInt(subjectToken);
      } catch (NumberFormatException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      UserDashboardResponse userEntity = userService.findById(userId);

      // Lista de autorizações do usuário
      List<SimpleGrantedAuthority> authorities = new ArrayList<>();

      if (userEntity.getProfileType().isAdmin()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      }
      if (userEntity.getProfileType().isPatient()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_PATIENT"));
      }
      if (userEntity.getProfileType().isProfessional()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSIONAL"));
      }

      request.setAttribute("user_id", userId);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userEntity, null,
          authorities);
      // Abaixo: Usado para que em todas as requisições o spring security possa
      // validar as informações do usuário
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);

  }

}
