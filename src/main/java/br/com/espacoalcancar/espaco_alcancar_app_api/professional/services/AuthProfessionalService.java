package br.com.espacoalcancar.espaco_alcancar_app_api.professional.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.AuthProfessionalRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.AuthProfessionalResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.repositories.ProfessionalRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthProfessionalService {

  @Autowired
  private ProfessionalRepository professionalRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  ProfessionalService professionalService;

  @Autowired
  private JWTProvider jwt;

  // Autenticação do profissional
  public AuthProfessionalResponse execute(AuthProfessionalRequest authProfessionalRequest)
      throws AuthenticationException {

    ProfessionalEntity professional = this.professionalRepository.findByEmail(authProfessionalRequest.getEmail());

    var passwordMatches = this.passwordEncoder.matches(authProfessionalRequest.getPassword(),
        professional.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException("User/password incorrect.");
    }

    // Gerando token e refreshToken
    String token = jwt.generateToken(professional.getId(), Instant.now().plus(Duration.ofHours(2)));
    String refreshToken = jwt.generateToken(professional.getId(), Instant.now().plus(Duration.ofDays(15)));

    List<String> roles = professionalService.getUserRoles(authProfessionalRequest.getEmail());

    AuthProfessionalResponse authProfessionalResponse = new AuthProfessionalResponse();
    authProfessionalResponse.setToken(token);
    authProfessionalResponse.setRefreshToken(refreshToken);
    authProfessionalResponse.setRoles(roles);

    return authProfessionalResponse;
  }

  // Atualizar token usando refreshToken
  public String refreshToken(String refreshToken) throws AuthenticationException {
    UUID professionalId = UUID.fromString(jwt.validateToken(refreshToken));
    if (professionalId == null) {
      throw new AuthenticationException("Invalid refresh token.");
    }

    // Gerando novo token
    String newToken = jwt.generateToken(professionalId, Instant.now().plus(Duration.ofHours(2)));
    return newToken;
  }
}
