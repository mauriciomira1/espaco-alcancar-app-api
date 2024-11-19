package br.com.espacoalcancar.espaco_alcancar_app_api.professional.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.NewProfessionalDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.ProfessionalService;

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
  public ResponseEntity<String> archive(Integer id) {
    try {
      professionalService.archiveProfessional(id);
      return ResponseEntity.ok().body("Profissional arquivado com sucesso");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao arquivar profissional");
    }
  }

  // Desarquivar um profissional
  public ResponseEntity<String> unarchive(Integer id) {
    try {
      professionalService.unarchiveProfessional(id);
      return ResponseEntity.ok().body("Profissional desarquivado com sucesso");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao desarquivar profissional");
    }
  }

  // Buscar um profissional
  public ResponseEntity<ProfessionalEntity> get(Integer id) {
    try {
      return ResponseEntity.ok().body(professionalService.getProfessional(id));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  // Atualizar um profissional
  public ResponseEntity<String> update(Integer id, NewProfessionalDTO request) {
    try {
      professionalService.updateProfessional(id, request);
      return ResponseEntity.ok().body("Profissional atualizado com sucesso");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar profissional");
    }
  }
}
