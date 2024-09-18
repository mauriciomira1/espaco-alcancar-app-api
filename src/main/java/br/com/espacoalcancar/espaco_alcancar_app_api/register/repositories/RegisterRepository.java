package br.com.espacoalcancar.espaco_alcancar_app_api.register.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.register.models.entities.RegisterEntity;

public interface RegisterRepository extends JpaRepository<RegisterEntity, Integer> {

}
