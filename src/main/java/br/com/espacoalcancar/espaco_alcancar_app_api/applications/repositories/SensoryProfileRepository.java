package br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;

public interface SensoryProfileRepository extends JpaRepository<SensoryProfileEntity, Integer> {

  public List<SensoryProfileEntity> findAllByProfessionalId(Integer professionalId);

}
