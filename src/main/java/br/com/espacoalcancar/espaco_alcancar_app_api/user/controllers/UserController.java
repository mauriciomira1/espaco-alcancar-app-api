package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserDashboardResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService userService;

  UUID userId;

  // Criar novo usuário paciente
  @PostMapping("/new")
  public UUID create(@Valid @RequestBody UserRequest request) {
    userId = userService.create(request);
    return userId;
  }

  // Buscar usuário
  @GetMapping("/me")
  public UserEntity getCurrentUser() {
    return userService.getCurrentUser();
  }

  // Listar todos os usuários cadastrados
  @GetMapping("/all")
  public List<UserResponse> listAll() {
    return userService.listAll();
  }

  // Editar dados do usuário
  @PutMapping("/edit")
  public UserEntity updateUser(@Valid @RequestBody UserRequest request) {
    // Obtendo o objeto de autenticação atual
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    var principal = (UserDashboardResponse) authentication.getPrincipal();
    if (!(principal instanceof UserDashboardResponse)) {
      throw new RuntimeException("User principal is not of expected type");
    }

    var userId = principal.getId();
    UserEntity updatedUser = userService.updateUser(userId, request);
    return updatedUser;
  }

}
