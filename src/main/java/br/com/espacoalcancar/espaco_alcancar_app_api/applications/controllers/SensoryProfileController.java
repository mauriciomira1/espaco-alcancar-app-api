package br.com.espacoalcancar.espaco_alcancar_app_api.applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsRequestDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.services.ResultsOfSensoryProfileService;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.services.SensoryProfileService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  // Preencher um perfil sensorial (perfil: paciente)
  @PutMapping("/sensory-profile")
  public void fillOut(@Valid @RequestBody ResultsRequestDTO results) {
    resultsOfSensoryProfileService.create(results);
  }

  // Listar todos os perfis sensoriais de um profissional (perfil: profissional)
  @GetMapping("/list-all-sensory-profiles-of-a-professional/{professionalId}")
  public List<SensoryProfileResponse> listAllSensoryProfilesOfAProfessional(@PathVariable Integer professionalId) {
    return sensoryProfileService.listAllByProfessional();
  }

  // Listar todos os perfis sensoriais de um profissional logado
  @GetMapping("/list-all-sensory-profiles-created")
  public ResponseEntity<List<SensoryProfileResponse>> listAllSensoryProfilesCreated() {
    try {
      List<SensoryProfileResponse> response = sensoryProfileService.listAllByProfessional();
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }

  }

  // Listar todos os perfis sensoriais cadastrados (perfil: admin)
  @GetMapping("/list-all-sensory-profiles")
  public ResponseEntity<List<SensoryProfileEntity>> listAllSensoryProfiles() {
    List<SensoryProfileEntity> response = sensoryProfileService.listAll();
    return ResponseEntity.ok().body(response);
  }

  // Preencher um perfil sensorial (perfil: paciente)
  @PutMapping("/fill-out-sensory-profile")
  public ResponseEntity<String> fillOutSensoryProfile(@Valid @RequestBody ResultsRequestDTO results) {
    try {
      resultsOfSensoryProfileService.create(results);
      return ResponseEntity.ok().body("Perfil sensorial preenchido com sucesso.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Erro ao preencher perfil sensorial.");
    }
  }

}
