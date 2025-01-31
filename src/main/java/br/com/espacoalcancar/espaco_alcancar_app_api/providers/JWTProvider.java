package br.com.espacoalcancar.espaco_alcancar_app_api.providers;

import java.time.Instant;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class JWTProvider {

  @Value("${security.token.secret}")
  private String secretKey;

  public String generateToken(UUID userId, Instant expiration) {
    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    var token = JWT
        .create()
        .withIssuer("espaco-alcancar")
        .withExpiresAt(expiration)
        .withSubject(userId.toString())
        .sign(algorithm);

    return token;
  }

  public String validateToken(String token) throws AuthenticationException {
    token = token.replace("Bearer ", "");
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    try {
      var subject = JWT.require(algorithm)
          .build()
          .verify(token)
          .getSubject();
      return subject; // ID do usuário
    } catch (JWTVerificationException e) {
      throw new AuthenticationException(e.getMessage());
    }
  }
}
