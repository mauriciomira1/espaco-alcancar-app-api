package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.naming.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;

@Service
public class AuthUserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService userService;

  @Value("${security.token.secret}")
  private String secretKey;

  // Autenticação do usuário
  public AuthUserResponse execute(AuthUserRequest authUserRequest) throws AuthenticationException {

    var user = this.userRepository.findByEmail(authUserRequest.getEmail())
        .orElseThrow(() -> {
          throw new UsernameNotFoundException("Username/password incorrect.");
        });

    // Verificar se senhas são iguais
    var passwordMatches = this.passwordEncoder.matches(authUserRequest.getPassword(), user.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException("User/password incorrect.");
    }

    List<String> roles = userService.getUserRoles(user.getEmail());

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    var token = JWT
        .create()
        .withIssuer("espaco-alcancar")
        .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
        .withSubject(user.getId().toString())
        .sign(algorithm);

    AuthUserResponse response = new AuthUserResponse();
    response.setToken(token);
    response.setRoles(roles);
    return response;

  }
}
