package br.com.espacoalcancar.espaco_alcancar_app_api.professional.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;

public interface ProfessionalRepository extends JpaRepository<ProfessionalEntity, Integer> {

  public List<ProfessionalEntity> findByActiveTrue();

  public ProfessionalEntity findByEmail(String email);

}