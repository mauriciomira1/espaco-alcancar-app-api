package br.com.espacoalcancar.espaco_alcancar_app_api.professional.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.EditProfessionalDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.NewProfessionalDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.ProfessionalDashboardResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.ProfessionalService;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/professional")
public class ProfessionalController {

  @Autowired
  ProfessionalService professionalService;

  // Criar um profissional
  @PostMapping("/new")
  public ResponseEntity<String> create(@RequestBody NewProfessionalDTO request) {
    try {
      professionalService.addProfessional(request);
      return ResponseEntity.status(HttpStatus.CREATED).body("Profissional cadastrado com sucesso");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email já cadastrado");
    }
  }

  // Arquivar um profissional
  @PutMapping("/archive")
  public ResponseEntity<String> archive(UUID id) {
    try {
      professionalService.archiveProfessional(id);
      return ResponseEntity.ok().body("Profissional arquivado com sucesso");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao arquivar profissional");
    }
  }

  // Desarquivar um profissional
  @PutMapping("/unarchive")
  public ResponseEntity<String> unarchive(UUID id) {
    try {
      professionalService.unarchiveProfessional(id);
      return ResponseEntity.ok().body("Profissional desarquivado com sucesso");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao desarquivar profissional");
    }
  }

  // Buscar um profissional
  @GetMapping("/get")
  public ResponseEntity<ProfessionalEntity> get(UUID id) {
    try {
      return ResponseEntity.ok().body(professionalService.getProfessional(id));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  // Obter profissional atual
  @GetMapping("/me")
  public ResponseEntity<ProfessionalDashboardResponse> getCurrentProfessional() {
    try {
      ProfessionalEntity entity = professionalService.getCurrentProfessional();

      ProfessionalDashboardResponse response = new ProfessionalDashboardResponse();
      response.setId(entity.getId());
      response.setName(entity.getName());
      response.setEmail(entity.getEmail());
      response.setPhone(entity.getPhone());
      response.setPassword(entity.getPassword());
      response.setActive(entity.isActive());
      response.setBirth(entity.getBirth());
      response.setRegisterNumber(entity.getRegisterNumber());
      response.setOccupation(entity.getOccupation());
      response.setProfileType(new ProfileType(false, true, false));

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  // Atualizar um profissional
  @PutMapping("/edit")
  public ResponseEntity<String> update(@Valid @RequestBody EditProfessionalDTO request) {
    try {
      professionalService.updateProfessional(request);
      return ResponseEntity.ok().body("Profissional atualizado com sucesso");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar profissional: " + e);
    }
  }
}
