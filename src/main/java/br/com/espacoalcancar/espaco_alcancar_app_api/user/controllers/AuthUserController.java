package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.AuthUserService;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthUserController {

  @Autowired
  AuthUserService authService;

  @Autowired
  JWTProvider jwt;

  @Autowired
  UserService userService;

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

  // Recuperando login com firebase
  @PostMapping("/auth/firebase")
  public ResponseEntity<Object> firebaseLogin(@RequestBody Map<String, String> body) {
    try {
      String firebaseToken = body.get("firebaseToken");
      if (firebaseToken == null || firebaseToken.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firebase token is required.");
      }

      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

      String uid = decodedToken.getUid();
      String email = decodedToken.getEmail();

      // Buscar/Criar o usuário no banco de dados com esse uid e email que chamei aqui
      UUID userId = userService.findByEmail(email);

      if (userId == null) {
        // Se o usuário não existir, chamar aqui o firebase para criar o usuário

        // salvar também no banco de dados

        // buscar ID no banco
      }

      String jwtToken = this.jwt.generateToken(userId, Instant.now().plus(Duration.ofMinutes(60)));

      Map<String, String> response = new HashMap<>();
      response.put("token", jwtToken);
      return ResponseEntity.ok().body(response);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

  // Verificação de token válido
  @PostMapping("/auth/validate")
  public ResponseEntity<Object> validate(@RequestBody String token) {
    try {
      UUID userId = UUID.fromString(this.jwt.validateToken(token));
      if (userId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
      }

      return ResponseEntity.ok().body("Valid token.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

  // Atualização do token
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
      String newToken = this.jwt.generateToken(userId, Instant.now().plus(Duration.ofMinutes(1)));
      String newRefreshToken = this.jwt.generateToken(userId, Instant.now().plus(Duration.ofDays(30)));

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
