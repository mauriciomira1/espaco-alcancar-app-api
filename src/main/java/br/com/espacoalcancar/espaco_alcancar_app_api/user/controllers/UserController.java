package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService userService;

  Integer userId;

  // Criar novo usuário
  @PostMapping("/new")
  public Integer create(@Valid @RequestBody UserRequest request) {
    userId = userService.create(request);
    return userId;
  }

  // Listar todos os usuários cadastrados
  @GetMapping("/all")
  public List<UserResponse> listAll() {
    return userService.listAll();
  }

  // Obter ID do Usuário
  @GetMapping("/{id}")
  public Integer getMethodName() {
    return userId;
  }
}
