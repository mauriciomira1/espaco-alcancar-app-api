package br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.ResultsOfSensoryProfile;

public interface ResultsOfSensoryProfileRepository extends JpaRepository<ResultsOfSensoryProfile, Integer> {

  // Procurar resultados de um perfil sensorial por id
  public ResultsOfSensoryProfile findBySensoryProfileId(Integer sensoryProfileId);
}
