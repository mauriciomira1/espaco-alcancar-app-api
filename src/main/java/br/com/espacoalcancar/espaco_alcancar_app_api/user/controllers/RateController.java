package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.RateRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.RateResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.RateService;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/dashboard/rate")
public class RateController {

  @Autowired
  RateService rateService;

  @Autowired
  UserService userService;

  // Adicionar uma avaliação
  @PostMapping("/new")
  public Integer create(@Valid @RequestBody RateRequest request, HttpServletRequest httpServletRequest) {

    Integer newRateId = this.rateService.create(request, httpServletRequest);
    return newRateId;
  }

  // Listar todas as avaliações de um usuário baseado no seu ID
  @GetMapping("/list-all")
  public Iterable<RateResponse> listAllRatesOfUserById() {
    Integer userId = userService.getCurrentUser().getId();

    return this.rateService.findByUserId(userId);
  }
}