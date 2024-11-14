package br.com.espacoalcancar.espaco_alcancar_app_api.applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsRequestDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.services.ResultsOfSensoryProfileService;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.services.SensoryProfileService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/dashboard/fillout")
public class SensoryProfileController {

  @Autowired
  SensoryProfileService sensoryProfileService;

  @Autowired
  ResultsOfSensoryProfileService resultsOfSensoryProfileService;

  // Criar um novo perfil sensorial (perfil: profissional)
  @PostMapping("/sensory-profile")
  public Integer create(@RequestBody SensoryProfileRequest request) {
    return sensoryProfileService.create(request);
  }

  // Preencher um perfil sensorial (paciente)
  @PutMapping("/sensory-profile")
  public void fillOut(@Valid @RequestBody ResultsRequestDTO results) {
    resultsOfSensoryProfileService.create(results);
  }

}
