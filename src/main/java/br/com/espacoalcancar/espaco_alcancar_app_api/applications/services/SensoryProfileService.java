package br.com.espacoalcancar.espaco_alcancar_app_api.applications.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Status;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.SensoryProfileRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.services.ProfessionalService;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.ChildRepository;

@Service
public class SensoryProfileService {

  @Autowired
  private SensoryProfileRepository sensoryProfileRepository;

  @Autowired
  private ChildRepository childRepository;

  @Autowired
  private ProfessionalService professionalService;

  // Criar novo perfil sensorial
  public Integer create(SensoryProfileRequest request) {

    ChildEntity child = childRepository.findById(request.getChildId())
        .orElseThrow(() -> new UsernameNotFoundException("Dependente não encontrado."));
    SensoryProfileEntity sensoryProfile = new SensoryProfileEntity();
    sensoryProfile.setProfessional(professionalService.getCurrentProfessional());
    sensoryProfile.setStatus(Status.UNFILLED);
    sensoryProfile.setChild(child);
    sensoryProfile.setProfileType(request.getProfileType());
    sensoryProfile.setCreatedAt(LocalDateTime.now());
    sensoryProfile.setUpdatedAt(LocalDateTime.now());
    sensoryProfile.setResultsOfSensoryProfile("");
    sensoryProfile = sensoryProfileRepository.save(sensoryProfile);
    return sensoryProfile.getId();
  }

  // Listar todos os perfis sensoriais
  public List<SensoryProfileEntity> listAll() {
    return sensoryProfileRepository.findAll();
  }

  // Listar todos os perfis sensoriais de um profissional logado
  public List<SensoryProfileResponse> listAllByProfessional() {
    ProfessionalEntity professional = professionalService.getCurrentProfessional();

    SensoryProfileResponse instant = new SensoryProfileResponse();
    List<SensoryProfileResponse> response = new ArrayList<>();
    List<SensoryProfileEntity> sensoryProfilesEntity = sensoryProfileRepository
        .findAllByProfessionalId(professional.getId());

    sensoryProfilesEntity.forEach(entity -> {
      instant.setId(entity.getId());
      instant.setChildName(entity.getChild().getName());
      instant.setProfileType(entity.getProfileType().toString());
      instant.setStatus(entity.getStatus().toString());
      instant.setCreatedAt(entity.getCreatedAt().toString());
      instant.setUpdatedAt(entity.getUpdatedAt().toString());
      instant.setResultsOfSensoryProfile(entity.getResultsOfSensoryProfile());
      response.add(instant);
    });

    return response;
  }

  // Função auxiliar para obter o número esperado de perguntas com base no tipo de
  // perfil sensorial
  private int getExpectedQuestions(SensoryProfileType profileType) {
    switch (profileType) {
      case UNTIL_THREE_YEARS:
        return 54;
      case MORE_THAN_THREE_YEARS:
        return 86;
      default:
        throw new IllegalArgumentException("Tipo de perfil sensorial desconhecido.");
    }
  }
}
