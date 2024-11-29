package br.com.espacoalcancar.espaco_alcancar_app_api.applications.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsRequestDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Status;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.SensoryProfileRepository;

@Service
public class ResultsOfSensoryProfileService {

  @Autowired
  SensoryProfileRepository sensoryProfileRepository;

  // Paciente responder um Perfil Sensorial
  public void create(ResultsRequestDTO results) {

    // Buscar o perfil sensorial disponibilizado a partir do seu ID
    SensoryProfileEntity sensoryProfile = sensoryProfileRepository.findById(results.getSensoryProfileId())
        .orElseThrow(() -> new UsernameNotFoundException("Sensory Profile not found"));

    // Alterar a lista de respostas (Answers)
    sensoryProfile.setResultsOfSensoryProfile(results.getAnswers());

    // Preenchimento de perfil (NÃO INICIADO, INICIADO ou FINALIZADO)
    if (sensoryProfile.getProfileType().toString() == "UNTIL_THREE_YEARS") {
      if (sensoryProfile.getResultsOfSensoryProfile().length() > 0
          && sensoryProfile.getResultsOfSensoryProfile().length() < 54) {
        sensoryProfile.setStatus(Status.STARTED);
      } else if (sensoryProfile.getResultsOfSensoryProfile().length() == 54) {
        sensoryProfile.setStatus(Status.FINISHED);
      }
    } else if (sensoryProfile.getProfileType().toString() == "MORE_THAN_THREE_YEARS") {
      if (sensoryProfile.getResultsOfSensoryProfile().length() > 0
          && sensoryProfile.getResultsOfSensoryProfile().length() < 86) {
        sensoryProfile.setStatus(Status.STARTED);
      } else if (sensoryProfile.getResultsOfSensoryProfile().length() == 86) {
        sensoryProfile.setStatus(Status.FINISHED);
      }
    }

    sensoryProfile.setUpdatedAt(LocalDateTime.now());

    sensoryProfileRepository.save(sensoryProfile);
  }
}