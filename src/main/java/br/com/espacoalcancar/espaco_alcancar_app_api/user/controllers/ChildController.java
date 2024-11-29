package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildUpdate;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserDashboardResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.ChildService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user/children")
public class ChildController {

  @Autowired
  ChildService childService;

  // Criar um novo filho/dependente
  @PostMapping("/new")
  public UUID create(@Valid @RequestBody ChildRequest childRequest, HttpServletRequest request) {
    return this.childService.create(childRequest, request);
  }

  // Buscar todos os filhos/dependentes
  @GetMapping("/list")
  public List<ChildResponse> list() {

    // buscar ID do usuário logado
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    var principal = (UserDashboardResponse) authentication.getPrincipal();
    if (!(principal instanceof UserDashboardResponse)) {
      throw new RuntimeException("User principal is not of expected type");
    }

    UUID userId = principal.getId();

    List<ChildResponse> childResponses = this.childService.list(userId);
    return childResponses;
  }

  // Listar todos os dependentes cadastrados
  @GetMapping("/list-all")
  public List<ChildResponse> listAll() {
    return childService.listAll();
  }

  // Atualizar um dependente
  @PutMapping("/edit")
  public UUID update(@Valid @RequestBody ChildUpdate childUpdate) {
    return this.childService.update(childUpdate);
  }

  // Remover um dependente
  @DeleteMapping("/remove")
  public void remove(@Valid @RequestBody UUID childId) {
    this.childService.remove(childId);
  }
}
