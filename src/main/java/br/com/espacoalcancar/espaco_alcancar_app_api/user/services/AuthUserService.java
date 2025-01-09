package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.naming.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;

import java.util.UUID;

@Service
public class AuthUserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService userService;

  @Autowired
  JWTProvider jwt;

  @Value("${security.token.secret}")
  private String secretKey;

  @Value("${security.refreshToken.secret}")
  private String refreshSecretKey;

  // Autenticação do usuário
  public AuthUserResponse execute(AuthUserRequest authUserRequest) throws AuthenticationException {

    UserEntity user = this.userRepository.findByEmail(authUserRequest.getEmail())
        .orElseThrow(() -> new AuthenticationException("Username/password incorrect."));

    // Verificar se senhas são iguais
    var passwordMatches = this.passwordEncoder.matches(authUserRequest.getPassword(), user.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException("User/password incorrect.");
    }

    List<String> roles = userService.getUserRoles(user.getEmail());

    // Gerando token e refreshToken
    String token = jwt.generateToken(user.getId(), Instant.now().plus(Duration.ofHours(2)));
    String refreshToken = jwt.generateToken(user.getId(), Instant.now().plus(Duration.ofDays(30)));

    AuthUserResponse response = new AuthUserResponse();
    response.setToken(token);
    response.setRefreshToken(refreshToken);
    response.setRoles(roles);

    return response;
  }

  // Atualizar token usando refreshToken
  public String refreshToken(String refreshToken) throws AuthenticationException {
    try {
      UUID userId = UUID.fromString(jwt.validateToken(refreshToken));
      if (userId == null) {
        throw new AuthenticationException("Invalid refresh token.");
      }

      // Gerando novo token
      String newToken = jwt.generateToken(userId, Instant.now().plus(Duration.ofHours(2)));
      return newToken;
    } catch (Exception e) {
      throw new AuthenticationException(e.getMessage());
    }
  }
}
