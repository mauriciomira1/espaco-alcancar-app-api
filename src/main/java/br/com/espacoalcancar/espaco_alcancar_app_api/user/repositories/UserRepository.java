package br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByName(String name);

}
