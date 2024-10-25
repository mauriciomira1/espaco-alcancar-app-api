package br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.RateEntity;

public interface RateRepository extends JpaRepository<RateEntity, Integer> {

  List<RateEntity> findAllByUserId(Integer userId);

}
