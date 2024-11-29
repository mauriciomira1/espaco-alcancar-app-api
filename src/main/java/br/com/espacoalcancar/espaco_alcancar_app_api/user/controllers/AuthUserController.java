package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.AuthUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthUserController {

  @Autowired
  AuthUserService authService;

  @Autowired
  JWTProvider jwt;

  // Login de usuário
  @PostMapping("/auth")
  public ResponseEntity<Object> create(@RequestBody AuthUserRequest request, HttpServletResponse response)
      throws AuthenticationException {
    try {
      AuthUserResponse result = this.authService.execute(request);

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

  @PostMapping("/auth/refresh")
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

      UUID userId = UUID.fromString(this.jwt.validateToken(refreshToken));
      if (userId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
      }

      // Gerando novos tokens
      String newToken = this.jwt.generateToken(userId, Instant.now().plus(Duration.ofHours(2)));
      String newRefreshToken = this.jwt.generateToken(userId,
          Instant.now().plus(Duration.ofDays(30)));

      // Atualizando o refresh token no cookie
      Cookie newRefreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
      newRefreshTokenCookie.setHttpOnly(true);
      newRefreshTokenCookie.setSecure(true);
      newRefreshTokenCookie.setPath("/");
      newRefreshTokenCookie.setMaxAge((int) Duration.ofDays(30).getSeconds());
      response.addCookie(newRefreshTokenCookie);

      return ResponseEntity.ok().body(newToken);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }
}