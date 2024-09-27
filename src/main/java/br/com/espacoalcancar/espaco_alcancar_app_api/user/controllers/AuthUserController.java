package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.AuthUserService;

@RestController
public class AuthUserController {

  @Autowired
  AuthUserService authUserUseCase;

  // Login de usuário
  @PostMapping("/auth")
  public ResponseEntity<Object> create(@RequestBody AuthUserRequest request) throws AuthenticationException {
    try {
      var result = this.authUserUseCase.execute(request);
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }
}
