package br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.ResultsOfSensoryProfileEntity;

public interface ResultsOfSensoryProfileRepository extends JpaRepository<ResultsOfSensoryProfileEntity, Integer> {

  // Procurar resultados de um perfil sensorial por id
  public ResultsOfSensoryProfileEntity findBySensoryProfileId(Integer sensoryProfileId);
}
