package br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;

public interface ChildRepository extends JpaRepository<ChildEntity, Integer> {

  Iterable<ChildEntity> findAllByUserId(Integer userId);

}
