package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserDashboardResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.ChildService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user/children")
public class ChildController {

  @Autowired
  ChildService childService;

  // Criar um novo filho/dependente
  @PostMapping("/new")
  public Integer create(@Valid @RequestBody ChildRequest childRequest, HttpServletRequest request) {
    return this.childService.create(childRequest, request);
  }

  // Buscar todos os filhos/dependentes
  @GetMapping("/list-all")
  public List<ChildEntity> listAll() {

    // buscar ID do usuário logado
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    var principal = (UserDashboardResponse) authentication.getPrincipal();
    if (!(principal instanceof UserDashboardResponse)) {
      throw new RuntimeException("User principal is not of expected type");
    }

    var userId = principal.getId();

    Iterable<ChildEntity> iterable = this.childService.listAll(userId);
    List<ChildEntity> list = new ArrayList<>();
    iterable.forEach(list::add);
    return list;
  }

}
