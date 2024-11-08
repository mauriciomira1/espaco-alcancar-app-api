package br.com.espacoalcancar.espaco_alcancar_app_api.applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.SensoryProfileRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.services.SensoryProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/dashboard/fillout")
public class SensoryProfileController {

  @Autowired
  SensoryProfileService sensoryProfileService;

  // Criar um novo perfil sensorial (o profissional irá criar um perfil sensorial
  // para o paciente)
  @PostMapping("/sensory-profile")
  public Integer create(@RequestBody SensoryProfileRequest request) {

    return sensoryProfileService.create(request);
  }

  // Atualizar as respostas de um perfil sensorial (o paciente irá atualizar as
  // respostas de um perfil sensorial)

}
