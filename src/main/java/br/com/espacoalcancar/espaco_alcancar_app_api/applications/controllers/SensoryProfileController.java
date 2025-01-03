package br.com.espacoalcancar.espaco_alcancar_app_api.applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsRequestDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.CreateSensoryProfileTypeDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.services.ResultsOfSensoryProfileService;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.services.SensoryProfileService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/dashboard/sp")
public class SensoryProfileController {

  @Autowired
  SensoryProfileService sensoryProfileService;

  @Autowired
  ResultsOfSensoryProfileService resultsOfSensoryProfileService;

  // Criar um novo perfil sensorial (perfil: profissional)
  @PostMapping("/new")
  public ResponseEntity<String> create(@RequestBody CreateSensoryProfileTypeDTO childId) {
    try {
      sensoryProfileService.create(childId.getChildId());
      return ResponseEntity.status(HttpStatus.CREATED)
          .body("Perfil sensorial criado e disponibilizado com sucesso");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Erro ao cadastrar perfil sensorial. Tente mais tarde ou entre em contato com o suporte.");
    }
  }

  // Listar todos os perfis sensoriais de um profissional (perfil: admin)
  @GetMapping("/list-all-of-a-professional/{professionalId}")
  public List<SensoryProfileResponse> listAllSensoryProfilesOfAProfessional(@PathVariable String professionalId) {
    return sensoryProfileService.listAllByProfessional();
  }

  // Listar todos os perfis sensoriais de um profissional logado (perfil:
  // profissional)
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
  @GetMapping("/list-all")
  public ResponseEntity<List<SensoryProfileEntity>> listAllSensoryProfiles() {
    List<SensoryProfileEntity> response = sensoryProfileService.listAll();
    return ResponseEntity.ok().body(response);
  }

  // Listar perfis sensoriais de um dependente (perfil: paciente)
  @GetMapping("/list-all-of-a-child/{childId}")
  public ResponseEntity<?> listAllSensoryProfilesOfAChild(@PathVariable UUID childId) {
    try {
      List<SensoryProfileEntity> response = sensoryProfileService.listAllByChild(childId);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // Buscar um perfil sensorial pelo seu ID (perfil: profissional)
  @GetMapping("/find-sp/{id}")
  public ResponseEntity<?> findSensoryProfileById(@PathVariable UUID id) {
    try {
      SensoryProfileResponse response = sensoryProfileService.getSensoryProfile(id);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // Preencher um perfil sensorial (perfil: paciente)
  @PutMapping("/fillout")
  public ResponseEntity<String> fillOutSensoryProfile(@Valid @RequestBody ResultsRequestDTO results) {
    try {
      resultsOfSensoryProfileService.create(results);
      return ResponseEntity.ok().body("Perfil sensorial preenchido com sucesso.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Erro ao preencher perfil sensorial.");
    }
  }

  // Obter as perguntas do perfil sensorial
  @GetMapping("/get-questions")
  public ResponseEntity<?> getQuestions(@RequestParam SensoryProfileType sensoryProfileType) {
    try {
      List<String> questions = sensoryProfileService.getQuestions(sensoryProfileType);
      return ResponseEntity.ok().body(questions);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // Obter as respostas de um perfil sensorial por ID do perfil
  @GetMapping("/get-answers-by-sp-id")
  public ResponseEntity<?> getAnswersBySensoryProfileId(@RequestParam String id) {
    try {
      var answers = sensoryProfileService
          .calculateSensoryProfile(id);
      return ResponseEntity.ok().body(answers);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
