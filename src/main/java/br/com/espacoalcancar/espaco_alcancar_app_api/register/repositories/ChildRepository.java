package br.com.espacoalcancar.espaco_alcancar_app_api.register.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.register.models.entities.ChildEntity;

public interface ChildRepository extends JpaRepository<ChildEntity, Integer> {

}
