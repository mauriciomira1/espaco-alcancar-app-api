package br.com.espacoalcancar.espaco_alcancar_app_api.professional.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.NewProfessionalDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.ProfessionalService;

@RestController
@RequestMapping("/professional")
public class ProfessionalController {

  @Autowired
  ProfessionalService professionalService;

  // Criar um profissional
  @PostMapping("/new")
  public Integer create(@RequestBody NewProfessionalDTO request) {
    return professionalService.addProfessional(request);
  }

  // Arquivar um profissional
  public void archive(Integer id) {
    professionalService.archiveProfessional(id);
  }

}
