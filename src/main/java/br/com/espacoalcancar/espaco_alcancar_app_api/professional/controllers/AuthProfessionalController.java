package br.com.espacoalcancar.espaco_alcancar_app_api.professional.controllers;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.AuthProfessionalRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.AuthProfessionalResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.AuthProfessionalService;
import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthProfessionalController {

  @Autowired
  AuthProfessionalService authService;

  @Autowired
  JWTProvider jwt;

  // Login de profissional
  @PostMapping("/auth/professional")
  public ResponseEntity<Object> create(@RequestBody AuthProfessionalRequest request, HttpServletResponse response) {
    try {
      AuthProfessionalResponse result = this.authService.execute(request);

      Cookie refreshTokenCookie = new Cookie("refreshToken", result.getRefreshToken());
      refreshTokenCookie.setHttpOnly(true);
      refreshTokenCookie.setSecure(true);
      refreshTokenCookie.setPath("/");
      refreshTokenCookie.setMaxAge((int) Duration.ofDays(30).getSeconds());
      response.addCookie(refreshTokenCookie);

      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

  // Verificação de token válido
  @PostMapping("/auth/professional/validate")
  public ResponseEntity<Object> validate(@RequestBody String token) {
    try {
      String professionalId = this.jwt.validateToken(token);
      if (professionalId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
      }

      return ResponseEntity.ok().body(professionalId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

  // Atualização do token
  @PostMapping("/auth/professional/refresh")
  public ResponseEntity<Object> refresh(HttpServletRequest request, HttpServletResponse response) {
    try {
      // Obtendo o refresh token do cookie
      Cookie[] cookies = request.getCookies();
      String refreshToken = null;
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("refreshToken")) {
            refreshToken = cookie.getValue();
            break;
          }
        }
      }

      if (refreshToken == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found.");
      }

      String professionalId = this.jwt.validateToken(refreshToken);
      if (professionalId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
      }

      String newToken = this.authService.refreshToken(professionalId);

      return ResponseEntity.ok().body(newToken);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

}
