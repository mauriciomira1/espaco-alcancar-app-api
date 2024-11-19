package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.ProfessionalService;
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
  private final ProfessionalService professionalService;

  public SecurityFilter(JWTProvider jwtProvider, UserService userService, ProfessionalService professionalService) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
    this.professionalService = professionalService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
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
      Integer userId;
      try {
        userId = Integer.parseInt(subjectToken);
      } catch (NumberFormatException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      Object userEntity = null;
      try {
        userEntity = userService.findById(userId);
      } catch (UsernameNotFoundException e) {
        // Se não encontrar no UserService, tenta no ProfessionalService
        try {
          userEntity = professionalService.getProfessional(userId);
        } catch (UsernameNotFoundException ex) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter().write("User not found");
          return;
        }
      }
      List<SimpleGrantedAuthority> authorities = new ArrayList<>();
      if (userEntity instanceof UserDashboardResponse) {
        UserDashboardResponse user = (UserDashboardResponse) userEntity;
        if (user.getProfileType().isAdmin()) {
          authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (user.getProfileType().isPatient()) {
          authorities.add(new SimpleGrantedAuthority("ROLE_PATIENT"));
        }
      } else if (userEntity instanceof ProfessionalEntity) {
        ProfessionalEntity professional = (ProfessionalEntity) userEntity;
        if (professional.getProfileType().isProfessional()) {
          authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSIONAL"));
        }
      }
      request.setAttribute("user_id", userId);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userEntity, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }
}
