package br.com.espacoalcancar.espaco_alcancar_app_api.applications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.ResultsRequestDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.ResultsOfSensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.ResultsOfSensoryProfileRepository;

@Service
public class ResultsOfSensoryProfileService {

  @Autowired
  ResultsOfSensoryProfileRepository resultsRepository;

  // Paciente adicionar respostas a um perfil sensorial já existente
  public void create(ResultsRequestDTO results) {

    // Buscar o atual resultado baseado no seu id
    ResultsOfSensoryProfileEntity currentResults = resultsRepository.findById(results.getResultId())
        .orElseThrow(() -> new IllegalArgumentException("Resultados do perfil sensorial não encontrados"));

    // Alterar a lista de respostas (Answers)
    currentResults.setAnswers(results.getAnswers());

    // Salvar as alterações no banco
    resultsRepository.save(currentResults);
  }
}
