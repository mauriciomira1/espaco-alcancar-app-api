package br.com.espacoalcancar.espaco_alcancar_app_api.applications.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Status;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.SensoryProfileRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.ChildRepository;

@Service
public class SensoryProfileService {

  @Autowired
  private SensoryProfileRepository sensoryProfileRepository;

  @Autowired
  private ChildRepository childRepository;

  // Criar novo perfil sensorial
  public Integer create(SensoryProfileRequest request) {

    SensoryProfileEntity sensoryProfile = new SensoryProfileEntity();
    sensoryProfile.setStatus(Status.UNFILLED);
    sensoryProfile.setChild(childRepository.findById(request.getChildId()).get());
    sensoryProfile.setProfileType(request.getProfileType());
    sensoryProfile.setCreatedAt(LocalDateTime.now());
    sensoryProfile.setUpdatedAt(LocalDateTime.now());
    sensoryProfile.setResultsOfSensoryProfile("");

    sensoryProfile = sensoryProfileRepository.save(sensoryProfile);

    return sensoryProfile.getId();
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
